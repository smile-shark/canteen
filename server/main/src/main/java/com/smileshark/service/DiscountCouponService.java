package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCoupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smileshark.entity.DiscountCouponCustomer;

import java.util.List;

/**
 * <p>
 * 优惠券 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface DiscountCouponService extends IService<DiscountCoupon> {

    Result<Page<DiscountCoupon>> pageList(Integer page, Integer size, Integer type, String shopId);

    Result<?> add(DiscountCoupon discountCoupon);

    Result<List<DiscountCoupon>> sendCouponList();

    Result<?> sendCoupon(String couponId, String customerId);

    Result<List<DiscountCouponCustomer>> usableCoupon(String shopId, List<String> cuisineIds, Double price);
}
