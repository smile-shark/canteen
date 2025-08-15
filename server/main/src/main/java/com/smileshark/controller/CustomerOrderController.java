package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.CustomerOrder;
import com.smileshark.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Result<CustomerOrder> takeOutAndDineInOrder(@RequestParam(required = false) String cuisineId,@RequestParam Integer orderType,@RequestParam(required = false) Boolean isAdd){
        return customerOrderService.takeOutAndDineInOrder( cuisineId, orderType,isAdd);
    }
    // 外卖和自提点餐
    @PostMapping("/dineInOrder")
    public Result<CustomerOrder> takeOutAndDineInOrder(@RequestParam(required = false) String cuisineId,@RequestParam Integer orderType,@RequestParam(required = false) Boolean isAdd, @RequestParam String diningTableId){
        return customerOrderService.takeOutAndDineInOrder( cuisineId, orderType,isAdd,diningTableId);
    }
    // 用户下单，返回订单的id
    @PostMapping("/placeOrderNow")
    public Result<String> placeOrderNow(@RequestParam Integer orderType,@RequestParam String diningTableId,@RequestParam(required = false)String discountCouponCustomerId,@RequestParam(required = false)String deliveryAddressId,@RequestParam(required = false)Double packingCharges,@RequestParam(required = false)Double deliveryCost){
        return customerOrderService.placeOrderNow(orderType,diningTableId,discountCouponCustomerId,deliveryAddressId,packingCharges,deliveryCost);
    }
}
