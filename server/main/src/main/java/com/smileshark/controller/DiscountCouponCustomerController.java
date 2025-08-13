package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCouponCustomer;
import com.smileshark.service.DiscountCouponCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户拥有的优惠券，优惠券和客户的中间表 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/discountCouponCustomer")
@RequiredArgsConstructor
public class DiscountCouponCustomerController {
    private final DiscountCouponCustomerService discountCouponCustomerService;
    @GetMapping("/list")
    public Result<List<DiscountCouponCustomer>> list(
            @RequestParam Integer state
    ){
        return discountCouponCustomerService.discountCouponCustomerList(state);
    }
}
