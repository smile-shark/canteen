package com.smileshark.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCoupon;
import com.smileshark.entity.DiscountCouponCustomer;
import com.smileshark.service.DiscountCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/add")
    public Result<?> add(@RequestBody DiscountCoupon discountCoupon){
        return discountCouponService.add(discountCoupon);
    }
    // 获取可以发放，没有过期，还有剩余的优惠券
    @GetMapping("/sendCouponList")
    public Result<List<DiscountCoupon>> sendCouponList(){
        return discountCouponService.sendCouponList();
    }
    @PostMapping("/sendCoupon")
    public Result<?> sendCoupon(@RequestParam String couponId,@RequestParam String customerId){
        return  discountCouponService.sendCoupon(couponId,customerId);
    }
    @GetMapping("/usableCoupon")
    public Result<List<DiscountCouponCustomer>> usableCoupon(@RequestParam String shopId, @RequestParam List<String> cuisineIds, @RequestParam Double price){
        return discountCouponService.usableCoupon(shopId, cuisineIds,price);
    }

}
