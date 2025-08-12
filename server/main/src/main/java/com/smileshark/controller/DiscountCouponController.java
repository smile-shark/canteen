package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCoupon;
import com.smileshark.service.DiscountCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 优惠券 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/discountCoupon")
@RequiredArgsConstructor
public class DiscountCouponController {
    private final DiscountCouponService discountCouponService;
    @GetMapping("/pageList")
    public Result<Page<DiscountCoupon>> pageList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String shopId
    ){
        return discountCouponService.pageList(page, size, type, shopId);
    }
}
