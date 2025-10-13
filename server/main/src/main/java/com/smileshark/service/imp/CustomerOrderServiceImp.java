package com.smileshark.service.imp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.config.RabbitMQConfig;
import com.smileshark.entity.*;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.*;
import com.smileshark.service.CustomerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.InfoThreadLocal;
import com.smileshark.utils.RedisKeyUtils;
import com.smileshark.utils.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.smileshark.code.RedisKey.*;

/**
 * <p>
 * 客户订单 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class CustomerOrderServiceImp extends ServiceImpl<CustomerOrderMapper, CustomerOrder> implements CustomerOrderService {
    private final StringRedisTemplate stringRedisTemplate;
    private final CuisineMapper cuisineMapper;
    private final DailySalesVolumeMapper dailySalesVolumeMapper;
    private final DiscountCouponCustomerMapper discountCouponCustomerMapper;
    private final DiscountCouponMapper discountCouponMapper;
    private final CustomerMapper customerMapper;
    private final RabbitTemplate rabbitTemplate;
    private final WalletMapper walletMapper;
    private final WalletTransactionRecordMapper walletTransactionRecordMapper;
    private final DiningTableMapper diningTableMapper;
    private final CustomerOrderCuisineMapper customerOrderCuisineMapper;

    @Override
    @Transactional
    public Result<CustomerOrder> takeOutAndDineInOrder(String cuisineId, Integer orderType, Boolean isAdd) {
        Customer customer = InfoThreadLocal.getCustomer();
        String json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(TAKE_OUT, String.valueOf(orderType), customer.getCustomerId()));
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
                order.setShopId(cuisine.getShopId());
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
                stringRedisTemplate.delete(RedisKeyUtils.formatKey(TAKE_OUT, String.valueOf(orderType), customer.getCustomerId()));
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
                stringRedisTemplate.opsForValue().set(RedisKeyUtils.formatKey(TAKE_OUT, String.valueOf(orderType), customer.getCustomerId()), JSONUtil.toJsonStr(order));
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
                order.setShopId(cuisine.getShopId());
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
    @Transactional
    public Result<String> placeOrderNow(Integer orderType, String diningTableId, String discountCouponCustomerId, String deliveryAddressId, Double packingCharges, Double deliveryCost) {
        String json;
        // 如果diningTableId为空那么说明是个人点餐不是堂食
        if (diningTableId == null || diningTableId.isEmpty()) {
            // 个人
            json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(TAKE_OUT, String.valueOf(orderType), InfoThreadLocal.getCustomer().getCustomerId()));
        } else {
            json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(DINE_IN, String.valueOf(orderType), diningTableId));
        }
        if (json == null) {
            return Result.error(ResultCode.NOT_HAVE_ORDER);
        }
        // 转换格式
        CustomerOrder customerOrder = JSONUtil.toBean(json, CustomerOrder.class);
        // 判断是否传递了优惠券
        if (discountCouponCustomerId != null && !discountCouponCustomerId.isEmpty()) {
            customerOrder.setDiscountCouponCustomerId(discountCouponCustomerId);
            // 查询该优惠券
            DiscountCouponCustomer discountCouponCustomer = discountCouponCustomerMapper.selectById(discountCouponCustomerId);
            if (discountCouponCustomer == null) {
                throw new BusinessException(ResultCode.NOT_HAVE_DISCOUNT_COUPON);
            }
            if (discountCouponCustomer.getState() == 1) {
                throw new BusinessException(ResultCode.COUPON_USED);
            }
            if (discountCouponCustomer.getState() == 2) {
                throw new BusinessException(ResultCode.COUPON_EXPIRED);
            }
            // 修改优惠券的状态
            discountCouponCustomer.setState(1);
            discountCouponCustomer.setUseTime(LocalDateTime.now());
            discountCouponCustomerMapper.updateById(discountCouponCustomer);
            // 计算新的价格
            DiscountCoupon discountCoupon = discountCouponMapper.selectById(discountCouponCustomer.getDiscountCouponId());
            // 判断优惠券类型
            if (discountCoupon.getType() == 0) {
                customerOrder.setAllPrice(customerOrder.getAllPrice() - discountCoupon.getPrice());
            } else if (discountCoupon.getType() == 1) {
                customerOrder.setAllPrice((customerOrder.getAllPrice() * discountCoupon.getDiscount()) / 10);
            }
        }
        // 补全数据
        customerOrder.setDiningTableId(diningTableId);
        customerOrder.setDeliveryAddressId(deliveryAddressId);
        customerOrder.setType(orderType);
        customerOrder.setCreateTime(LocalDateTime.now());
        customerOrder.setCustomerId(InfoThreadLocal.getCustomer().getCustomerId());
        customerOrder.setPackingCharges(packingCharges); // 补充打包费
        customerOrder.setDeliveryCost(deliveryCost); // 补充配送费
        customerOrder.setState(2);
        // 日期格式化为yyyMMdd
        String customerOrderId = DateUtil.format(new Date(), "yyyyMMdd") + IdUtil.simpleUUID();
        customerOrder.setCustomerOrderId(customerOrderId);
        // 设置为待支付订单，并存入redis，不用设置时限，15分钟后死信队列会处理这个数据，rabbitmq设置超时的死性队列
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.formatKey(PLACE_ORDER, customerOrderId), JSONUtil.toJsonStr(customerOrder));
        // 发送到rabbitmq
        rabbitTemplate.convertAndSend(RabbitMQConfig.BUSINESS_EXCHANGE, RabbitMQConfig.BUSINESS_ROUTING_KEY, customerOrderId);

        // 删除redis中存在的订单
        stringRedisTemplate.delete(RedisKeyUtils.formatKey(TAKE_OUT, String.valueOf(orderType), InfoThreadLocal.getCustomer().getCustomerId()));
        stringRedisTemplate.delete(RedisKeyUtils.formatKey(DINE_IN, String.valueOf(orderType), diningTableId));
        return Result.success(customerOrderId);
    }

    @Override
    public Result<CustomerOrder> placeOrderGet(String customerOrderId) {
        // 从redis中获取数据
        String json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(PLACE_ORDER, customerOrderId));
        if (json == null) {
            throw new BusinessException(ResultCode.NOT_HAVE_ORDER);
        }
        return Result.success(JSONUtil.toBean(json, CustomerOrder.class));
    }

    @Override
    @Transactional
    public Result<?> payOrder(String customerOrderId, Integer payType) {
        Customer customer = InfoThreadLocal.getCustomer();
        // 从redis中获取数据
        String json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(PLACE_ORDER, customerOrderId));
        if (json == null) {
            throw new BusinessException(ResultCode.NOT_HAVE_ORDER);
        }
        CustomerOrder customerOrder = JSONUtil.toBean(json, CustomerOrder.class);
        customerOrder.setPayType(payType);
        customerOrder.setSource(customerOrder.getType() == 0 ? 0 : 1);
        // 判断支付的方式
        if (payType.equals(4)) {
            // 会员卡支付
            // 检查会员卡余额
            Wallet wallet = walletMapper.selectById(customer.getWalletId());
            if (wallet == null) {
                throw new BusinessException(ResultCode.NOT_HAVE_WALLET);
            }
            if (wallet.getBalance() < customerOrder.getAllPrice()) {
                throw new BusinessException(ResultCode.NOT_ENOUGH_MONEY);
            }
            // 扣减余额
            wallet.setBalance(wallet.getBalance() - customerOrder.getAllPrice());
            walletMapper.updateById(wallet);
            // 添加交易记录
            walletTransactionRecordMapper.insert(new WalletTransactionRecord(IdUtil.simpleUUID(), wallet.getWalletId(), 1, customerOrder.getAllPrice(), customerOrder.getType() == 0 ? 1 : 0));
        }
        // 添加菜品对应的销量
        for (CustomerOrderCuisine customerOrderCuisine : customerOrder.getCustomerOrderCuisines()) {
            // 查找今天这个菜品是否有销量的数据
            DailySalesVolume dailySalesVolume = dailySalesVolumeMapper.selectOne(new LambdaQueryWrapper<>(DailySalesVolume.class).eq(DailySalesVolume::getCuisineId, customerOrderCuisine.getCuisineId()).eq(DailySalesVolume::getSalesDate, LocalDate.now()));
            if (dailySalesVolume == null) {
                // 添加今天的数据
                dailySalesVolumeMapper.insert(new DailySalesVolume(customerOrderCuisine.getCuisineId(), LocalDate.now(), Long.valueOf(customerOrderCuisine.getNum())));
            } else {
                // 更新数据
                dailySalesVolumeMapper.update(new LambdaUpdateWrapper<>(DailySalesVolume.class).setSql("num = num + " + customerOrderCuisine.getNum()).eq(DailySalesVolume::getCuisineId, customerOrderCuisine.getCuisineId())
                        .eq(DailySalesVolume::getSalesDate, LocalDate.now()));
            }
            // 添加到订单数据的明细表中
            customerOrderCuisine.setCustomerOrderId(customerOrder.getCustomerOrderId());
            customerOrderCuisineMapper.insert(customerOrderCuisine);
        }
        // 添加订单数据
        customerOrder.setPayTime(LocalDateTime.now());
        save(customerOrder);
        // 添加用户的消费数据
        customer.setIntegral((int) (customer.getIntegral()+customerOrder.getAllPrice()));
        customer.setConsumptionNum(customer.getConsumptionNum()==null?1:customer.getConsumptionNum() + 1);
        customer.setConsumptionPlance(customer.getConsumptionPlance() + customerOrder.getAllPrice());
        if(customer.getConsumptionPlance()>10000){
            customer.setLevel(2);
        }else if (customer.getConsumptionPlance()>1000){
            customer.setLevel(1);
        }
        customerMapper.updateById(customer);
        // 删除redis中的数据
        stringRedisTemplate.delete(RedisKeyUtils.formatKey(PLACE_ORDER, customerOrderId));
        return Result.success("支付成功");
    }

    @Override
    public Result<Page<CustomerOrder>> pageList(Integer page, Integer size, Integer type, String customerOrderId, String shopId, LocalDateTime createTimeStart, LocalDateTime createTimeEnd) {
        LambdaQueryChainWrapper<CustomerOrder> query = lambdaQuery().eq(CustomerOrder::getType, type).last("order by create_time desc").like(CustomerOrder::getCustomerOrderId, StrUtil.globbing(customerOrderId));
        if(shopId != null && !shopId.isEmpty()){
            query.eq(CustomerOrder::getShopId, shopId);
        }
        if(createTimeStart != null){
            query.ge(CustomerOrder::getCreateTime, createTimeStart);
        }
        if(createTimeEnd != null){
            query.le(CustomerOrder::getCreateTime, createTimeEnd);
        }
        Page<CustomerOrder> pageInfo = query.page(new Page<>(page, size));
        for (CustomerOrder record : pageInfo.getRecords()) {
           if(record.getDiningTableId()!=null){
               record.setTableNum(diningTableMapper.selectOne(new LambdaQueryWrapper<>(DiningTable.class).select(DiningTable::getSerialNumber).eq(DiningTable::getDiningTableId,record.getDiningTableId())).getSerialNumber());
           }
        }
        return Result.success(pageInfo);
    }
}

