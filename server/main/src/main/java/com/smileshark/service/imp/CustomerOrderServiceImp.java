package com.smileshark.service.imp;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Cuisine;
import com.smileshark.entity.Customer;
import com.smileshark.entity.CustomerOrder;
import com.smileshark.entity.CustomerOrderCuisine;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.CuisineMapper;
import com.smileshark.mapper.CustomerOrderMapper;
import com.smileshark.service.CustomerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.InfoThreadLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    @Transactional
    public Result<CustomerOrder> takeOutAndDineInOrder(String cuisineId, Integer orderType, Boolean isAdd) {
        Customer customer = InfoThreadLocal.getCustomer();
        String json = stringRedisTemplate.opsForValue().get("takeOutAndDineInOrder:" + orderType + ":" + customer.getCustomerId());
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
                throw new BusinessException(ResultCode.NUM_NOT_ENOUGH);
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
                throw new BusinessException(ResultCode.NOT_HAVE_ORDER);
            } else {
                // 订单已存在
                order = JSONUtil.toBean(json, CustomerOrder.class);
                // 查询订单中是否有该商品
                List<CustomerOrderCuisine> list = order.getCustomerOrderCuisines().stream().filter(customerOrderCuisine -> customerOrderCuisine.getCuisineId().equals(cuisineId)).toList();
                if (list.isEmpty()) {
                    // 订单中没有该菜品的数据
                    throw new BusinessException(ResultCode.NOT_HAVE_CUISINE);
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
                stringRedisTemplate.delete("takeOutAndDineInOrder:" + orderType + ":" + customer.getCustomerId());
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
                stringRedisTemplate.opsForValue().set("takeOutAndDineInOrder:" + orderType + ":" + customer.getCustomerId(), JSONUtil.toJsonStr(order));
            }
        }
        return Result.success(order);
    }
}
