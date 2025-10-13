package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.CustomerOrder;
import com.smileshark.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户订单 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/customerOrder")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;

    // 外卖和自提点餐
    @PostMapping("/takeOutAndDineInOrder")
    public Result<CustomerOrder> takeOutAndDineInOrder(@RequestParam(required = false) String cuisineId, @RequestParam Integer orderType, @RequestParam(required = false) Boolean isAdd) {
        return customerOrderService.takeOutAndDineInOrder(cuisineId, orderType, isAdd);
    }

    // 外卖和自提点餐
    @PostMapping("/dineInOrder")
    public Result<CustomerOrder> takeOutAndDineInOrder(@RequestParam(required = false) String cuisineId, @RequestParam Integer orderType, @RequestParam(required = false) Boolean isAdd, @RequestParam String diningTableId) {
        return customerOrderService.takeOutAndDineInOrder(cuisineId, orderType, isAdd, diningTableId);
    }

    // 用户下单，返回订单的id
    @PostMapping("/placeOrderNow")
    public Result<String> placeOrderNow(
            @RequestParam Integer orderType,    // 订单类型
            @RequestParam String diningTableId, // 餐桌
            @RequestParam(required = false) String discountCouponCustomerId, // 优惠券
            @RequestParam(required = false) String deliveryAddressId, // 收货地址
            @RequestParam(required = false) Double packingCharges, // 打包费
            @RequestParam(required = false) Double deliveryCost // 配送费
    ) {
        return customerOrderService.placeOrderNow(orderType, diningTableId, discountCouponCustomerId, deliveryAddressId, packingCharges, deliveryCost);
    }

    // 获取待支付的订单
    @GetMapping("/placeOrderGet")
    public Result<CustomerOrder> placeOrderGet(@RequestParam String customerOrderId) {
        return customerOrderService.placeOrderGet(customerOrderId);
    }

    // 支付订单
    @PostMapping("/payOrder")
    public Result<?> payOrder(@RequestParam String customerOrderId, @RequestParam Integer payType) {
        return customerOrderService.payOrder(customerOrderId, payType);
    }

    // 商户获取订单数据
    @GetMapping("/pageList")
    public Result<Page<CustomerOrder>> pageList(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam Integer type,
            @RequestParam(required = false) String customerOrderId,
            @RequestParam(required = false) String shopId,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  LocalDateTime createTimeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  LocalDateTime createTimeEnd
    ) {
        return customerOrderService.pageList(page, size, type, customerOrderId, shopId, createTimeStart, createTimeEnd);
    }
}
