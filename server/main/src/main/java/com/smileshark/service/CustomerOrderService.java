package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.CustomerOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户订单 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface CustomerOrderService extends IService<CustomerOrder> {

    Result<CustomerOrder> takeOutAndDineInOrder(String cuisineId, Integer orderType, Boolean isAdd);



    Result<CustomerOrder> takeOutAndDineInOrder(String cuisineId, Integer orderType, Boolean isAdd, String diningTableId);

    Result<String> placeOrderNow(Integer orderType, String diningTableId, String discountCouponCustomerId, String deliveryAddressId, Double packingCharges, Double deliveryCost);

    Result<CustomerOrder> placeOrderGet(String customerOrderId);

    Result<?> payOrder(String customerOrderId, Integer payType);

    Result<Page<CustomerOrder>> pageList(Integer page, Integer size, Integer type, String customerOrderId, String shopId, LocalDateTime createTimeStart, LocalDateTime createTimeEnd);
}
