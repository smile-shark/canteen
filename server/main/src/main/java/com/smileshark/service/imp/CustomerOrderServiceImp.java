package com.smileshark.service.imp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.config.RabbitMQConfig;
import com.smileshark.entity.*;
import com.smileshark.mapper.*;
import com.smileshark.service.CustomerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.InfoThreadLocal;
import com.smileshark.utils.RedisKeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.smileshark.code.RedisKey.*;

/**
 * <p>
 * 客户订单 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service@RequiredArgsConstructor
public class CustomerOrderServiceImp extends ServiceImpl<CustomerOrderMapper, CustomerOrder> implements CustomerOrderService {
    private final StringRedisTemplate stringRedisTemplate;
    private final CuisineMapper cuisineMapper;
    private final DiscountCouponCustomerMapper discountCouponCustomerMapper;
    private final DiscountCouponMapper discountCouponMapper;
    private final CustomerMapper customerMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Result<CustomerOrder> takeOutAndDineInOrder(String cuisineId, Integer orderType, Boolean isAdd) {
        Customer customer = InfoThreadLocal.getCustomer();
        String json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(TAKE_OUT,String.valueOf(orderType), customer.getCustomerId()));
        CustomerOrder order = null;
        // 刚进入点餐页面时会触发一次，此时就只有orderType没有其他的属性
        if (cuisineId == null || cuisineId.isEmpty()) {
            // 只需要得到订单的数据，没有就不管
            if (json != null) {
                order = JSONUtil.toBean(json, CustomerOrder.class);
            }
        } else if (isAdd) {
            // 判断菜品库存是否足够
            Cuisine cuisine = cuisineMapper.selectById(cuisineId);
            if (cuisine.getInventory() < 1) {
                return Result.error(ResultCode.NUM_NOT_ENOUGH);
            }
            // 减少库存数量
            cuisineMapper.update(new LambdaUpdateWrapper<>(Cuisine.class).setSql("inventory = inventory - 1").eq(Cuisine::getCuisineId, cuisineId));
            // 添加的逻辑
            if (json == null) {
                // 创建一个新的订单
                order = new CustomerOrder();
                order.setCustomerId(customer.getCustomerId());
                order.setCustomerOrderCuisines(List.of(new CustomerOrderCuisine(null, cuisineId, 1, 0)));
            } else {
                // 订单已存在，添加菜品
                order = JSONUtil.toBean(json, CustomerOrder.class);
                // 检查订单中是否有了该商品，有了则数量加1，没有则添加
                List<CustomerOrderCuisine> list = order.getCustomerOrderCuisines().stream().filter(customerOrderCuisine -> customerOrderCuisine.getCuisineId().equals(cuisineId)).toList();
                if (list.isEmpty()) {
                    // 订单中没有该菜品的数据
                    order.getCustomerOrderCuisines().add(new CustomerOrderCuisine(null, cuisineId, 1, order.getCustomerOrderCuisines().size()));
                } else {
                    // 订单中有该菜品的数据，数量加1
                    list.get(0).setNum(list.get(0).getNum() + 1);
                }
            }
        } else {
            // 减少的逻辑
            if (json == null) {
                // 订单不存在
                return Result.error(ResultCode.NOT_HAVE_ORDER);
            } else {
                // 订单已存在
                order = JSONUtil.toBean(json, CustomerOrder.class);
                // 查询订单中是否有该商品
                List<CustomerOrderCuisine> list = order.getCustomerOrderCuisines().stream().filter(customerOrderCuisine -> customerOrderCuisine.getCuisineId().equals(cuisineId)).toList();
                if (list.isEmpty()) {
                    // 订单中没有该菜品的数据
                    return Result.error(ResultCode.NOT_HAVE_CUISINE);
                } else {
                    // 减少数量，如果为1就删除该菜品
                    if (list.get(0).getNum() <= 1) {
                        // 删除该菜品
                        order.getCustomerOrderCuisines().remove(list.get(0));
                        // 如果订单中没有菜品了，则删除订单
                        if (order.getCustomerOrderCuisines().isEmpty()) {
                            order = null;
                        }
                    } else {
                        // 减少数量
                        list.get(0).setNum(list.get(0).getNum() - 1);
                    }

                    // 添加库存数量
                    cuisineMapper.update(new LambdaUpdateWrapper<>(Cuisine.class).setSql("inventory = inventory + 1").eq(Cuisine::getCuisineId, cuisineId));
                }
            }
        }
        // 如果order为空那么就删除订单，否则更新新的信息
        if (isAdd != null) {
            if (order == null) {
                stringRedisTemplate.delete(RedisKeyUtils.formatKey(TAKE_OUT,String.valueOf(orderType), customer.getCustomerId()));
            } else {

                // 计算订单的价格
                double orderPrice = 0;
                for (CustomerOrderCuisine customerOrderCuisine : order.getCustomerOrderCuisines()) {
                    Cuisine cuisine = cuisineMapper.selectById(customerOrderCuisine.getCuisineId());
                    // 判断这个菜品是否有特价，没有就使用常规的价格进行计算
                    double usePrice = 0;
                    if (cuisine.getIsSpecialOffer() == 1) {
                        usePrice = cuisine.getSpecialOffer() != null ? cuisine.getSpecialOffer() : cuisine.getPrice();
                    } else {
                        usePrice = cuisine.getPrice();
                    }
                    orderPrice += usePrice * customerOrderCuisine.getNum();
                }
                // TODO: 这里可能需要对优惠券的使用做一个判断
                order.setAllPrice(orderPrice);
                stringRedisTemplate.opsForValue().set(RedisKeyUtils.formatKey(TAKE_OUT,String.valueOf(orderType), customer.getCustomerId()), JSONUtil.toJsonStr(order));
            }
        }
        return Result.success(order);
    }


    @Override
    public Result<CustomerOrder> takeOutAndDineInOrder(String cuisineId, Integer orderType, Boolean isAdd, String diningTableId) {

        Customer customer = InfoThreadLocal.getCustomer();
        // 使用桌位号座位key
        String json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(DINE_IN, String.valueOf(orderType), diningTableId));
        CustomerOrder order = null;
        // 刚进入点餐页面时会触发一次，此时就只有orderType没有其他的属性
        if (cuisineId == null || cuisineId.isEmpty()) {
            // 只需要得到订单的数据，没有就不管
            if (json != null) {
                order = JSONUtil.toBean(json, CustomerOrder.class);
            }
        } else if (isAdd) {
            // 判断菜品库存是否足够
            Cuisine cuisine = cuisineMapper.selectById(cuisineId);
            if (cuisine.getInventory() < 1) {
                return Result.error(ResultCode.NUM_NOT_ENOUGH);
            }
            // 减少库存数量
            cuisineMapper.update(new LambdaUpdateWrapper<>(Cuisine.class).setSql("inventory = inventory - 1").eq(Cuisine::getCuisineId, cuisineId));
            // 添加的逻辑
            if (json == null) {
                // 创建一个新的订单
                order = new CustomerOrder();
                order.setCustomerId(customer.getCustomerId());
                order.setCustomerOrderCuisines(List.of(new CustomerOrderCuisine(null, cuisineId, 1, 0)));
            } else {
                // 订单已存在，添加菜品
                order = JSONUtil.toBean(json, CustomerOrder.class);
                // 检查订单中是否有了该商品，有了则数量加1，没有则添加
                List<CustomerOrderCuisine> list = order.getCustomerOrderCuisines().stream().filter(customerOrderCuisine -> customerOrderCuisine.getCuisineId().equals(cuisineId)).toList();
                if (list.isEmpty()) {
                    // 订单中没有该菜品的数据
                    order.getCustomerOrderCuisines().add(new CustomerOrderCuisine(null, cuisineId, 1, order.getCustomerOrderCuisines().size()));
                } else {
                    // 订单中有该菜品的数据，数量加1
                    list.get(0).setNum(list.get(0).getNum() + 1);
                }
            }
        } else {
            // 减少的逻辑
            if (json == null) {
                // 订单不存在
                return Result.error(ResultCode.NOT_HAVE_ORDER);
            } else {
                // 订单已存在
                order = JSONUtil.toBean(json, CustomerOrder.class);
                // 查询订单中是否有该商品
                List<CustomerOrderCuisine> list = order.getCustomerOrderCuisines().stream().filter(customerOrderCuisine -> customerOrderCuisine.getCuisineId().equals(cuisineId)).toList();
                if (list.isEmpty()) {
                    // 订单中没有该菜品的数据
                    return Result.error(ResultCode.NOT_HAVE_CUISINE);
                } else {
                    // 减少数量，如果为1就删除该菜品
                    if (list.get(0).getNum() <= 1) {
                        // 删除该菜品
                        order.getCustomerOrderCuisines().remove(list.get(0));
                        // 如果订单中没有菜品了，则删除订单
                        if (order.getCustomerOrderCuisines().isEmpty()) {
                            order = null;
                        }
                    } else {
                        // 减少数量
                        list.get(0).setNum(list.get(0).getNum() - 1);
                    }

                    // 添加库存数量
                    cuisineMapper.update(new LambdaUpdateWrapper<>(Cuisine.class).setSql("inventory = inventory + 1").eq(Cuisine::getCuisineId, cuisineId));
                }
            }
        }
        // 如果order为空那么就删除订单，否则更新新的信息
        if (isAdd != null) {
            if (order == null) {
                stringRedisTemplate.delete(RedisKeyUtils.formatKey(DINE_IN, String.valueOf(orderType), diningTableId));
            } else {

                // 计算订单的价格
                double orderPrice = 0;
                for (CustomerOrderCuisine customerOrderCuisine : order.getCustomerOrderCuisines()) {
                    Cuisine cuisine = cuisineMapper.selectById(customerOrderCuisine.getCuisineId());
                    // 判断这个菜品是否有特价，没有就使用常规的价格进行计算
                    double usePrice = 0;
                    if (cuisine.getIsSpecialOffer() == 1) {
                        usePrice = cuisine.getSpecialOffer() != null ? cuisine.getSpecialOffer() : cuisine.getPrice();
                    } else {
                        usePrice = cuisine.getPrice();
                    }
                    orderPrice += usePrice * customerOrderCuisine.getNum();
                }
                // TODO: 这里可能需要对优惠券的使用做一个判断
                order.setAllPrice(orderPrice);
                stringRedisTemplate.opsForValue().set(RedisKeyUtils.formatKey(DINE_IN, String.valueOf(orderType), diningTableId), JSONUtil.toJsonStr(order));
            }
        }
        return Result.success(order);
    }

    @Override
    public Result<String> placeOrderNow(Integer orderType, String diningTableId, String discountCouponCustomerId, String deliveryAddressId, Double packingCharges, Double deliveryCost) {
        String json;
        // 如果diningTableId为空那么说明是个人点餐不是堂食
        if(diningTableId == null || diningTableId.isEmpty()){
            // 个人
            json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(TAKE_OUT,String.valueOf(orderType), InfoThreadLocal.getCustomer().getCustomerId()));
        }else{
            json=stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(DINE_IN, String.valueOf(orderType), diningTableId));
        }
        if (json == null) {
            return Result.error(ResultCode.NOT_HAVE_ORDER);
        }
        // 删除redis中存在的订单
        stringRedisTemplate.delete(RedisKeyUtils.formatKey(TAKE_OUT,String.valueOf(orderType), InfoThreadLocal.getCustomer().getCustomerId()));
        stringRedisTemplate.delete(RedisKeyUtils.formatKey(DINE_IN, String.valueOf(orderType), diningTableId));
        // 转换格式
        CustomerOrder customerOrder = JSONUtil.toBean(json, CustomerOrder.class);
        // 判断是否传递了优惠券
        if(discountCouponCustomerId != null && !discountCouponCustomerId.isEmpty()){
            customerOrder.setDiscountCouponCustomerId(discountCouponCustomerId);
            // 修改优惠券的状态
            discountCouponCustomerMapper.update(new LambdaUpdateWrapper<>(DiscountCouponCustomer.class).set(DiscountCouponCustomer::getState, 1).eq(DiscountCouponCustomer::getDiscountCouponCustomerId, discountCouponCustomerId));
            // 计算新的价格
            DiscountCoupon discountCoupon = discountCouponMapper.selectById(discountCouponCustomerId);
            // 判断优惠券类型
            if(discountCoupon.getType()==0){
                customerOrder.setAllPrice(customerOrder.getAllPrice()-discountCoupon.getPrice());
            }else if (discountCoupon.getType() == 1){
                customerOrder.setAllPrice((customerOrder.getAllPrice()*discountCoupon.getDiscount())/10);
            }
        }
        // 补全数据
        customerOrder.setDiningTableId(diningTableId);
        customerOrder.setDeliveryAddressId(deliveryAddressId);
        customerOrder.setType(orderType);
        customerOrder.setCustomerId(InfoThreadLocal.getCustomer().getCustomerId());
        customerOrder.setPackingCharges(packingCharges); // 补充打包费
        customerOrder.setDeliveryCost(deliveryCost); // 补充配送费
        // 日期格式化为yyyMMdd
        String customerOrderId=DateUtil.format(new Date(), "yyyyMMdd")+ IdUtil.simpleUUID();
        customerOrder.setCustomerId(customerOrderId);
        // 设置为待支付订单，并存入redis，不用设置时限，15分钟后死信队列会处理这个数据，rabbitmq设置超时的死性队列
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.formatKey(PLACE_ORDER,customerOrderId),JSONUtil.toJsonStr(customerOrder));
        // 发送到rabbitmq
        rabbitTemplate.convertAndSend(RabbitMQConfig.BUSINESS_EXCHANGE,RabbitMQConfig.BUSINESS_ROUTING_KEY,customerOrderId);

        return Result.success(customerOrderId);
    }
}

