package com.smileshark.customer;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.smileshark.config.RabbitMQConfig;
import com.smileshark.entity.Cuisine;
import com.smileshark.entity.CustomerOrder;
import com.smileshark.entity.CustomerOrderCuisine;
import com.smileshark.entity.DiscountCouponCustomer;
import com.smileshark.mapper.CuisineMapper;
import com.smileshark.mapper.DiscountCouponCustomerMapper;
import com.smileshark.utils.RedisKeyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.smileshark.code.RedisKey.PLACE_ORDER;

@Component
@RequiredArgsConstructor
public class OrderErrorCustomer {
    private final StringRedisTemplate stringRedisTemplate;
    private final CuisineMapper cuisineMapper;
    private final DiscountCouponCustomerMapper discountCouponCustomerMapper;

    @RabbitListener(queues = RabbitMQConfig.DLX_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void processOrderError(String customerOrderId) {
        System.out.println("处理过期订单：" + customerOrderId);
        // 在redis中查询这个对应的订单，如果没有就说明订单已经处理完毕，可以忽略
        String json = stringRedisTemplate.opsForValue().get(RedisKeyUtils.formatKey(PLACE_ORDER, customerOrderId));
        if (json == null) {
            return;
        }
        // 处理过期的订单
        stringRedisTemplate.delete(RedisKeyUtils.formatKey(PLACE_ORDER, customerOrderId));
        CustomerOrder customerOrder = JSONUtil.toBean(json, CustomerOrder.class);
        // 1. 处理对应的菜品数据返回库存
        for (CustomerOrderCuisine customerOrderCuisine : customerOrder.getCustomerOrderCuisines()) {
            cuisineMapper.update(new LambdaUpdateWrapper<>(Cuisine.class).setSql("inventory = inventory + " + customerOrderCuisine.getNum()).eq(Cuisine::getCuisineId, customerOrderCuisine.getCuisineId()));
        }
        // 2. 恢复用户对应的优惠券的状态
        if(customerOrder.getDiscountCouponCustomerId()!= null && !customerOrder.getDiscountCouponCustomerId().isEmpty()){
            discountCouponCustomerMapper.update(new LambdaUpdateWrapper<>(DiscountCouponCustomer.class).set(DiscountCouponCustomer::getState,0).set(DiscountCouponCustomer::getUseTime,null).eq(DiscountCouponCustomer::getDiscountCouponCustomerId,customerOrder.getDiscountCouponCustomerId()));
        }
    }
}
