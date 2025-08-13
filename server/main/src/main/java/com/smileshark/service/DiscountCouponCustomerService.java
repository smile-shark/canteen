package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCouponCustomer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户拥有的优惠券，优惠券和客户的中间表 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface DiscountCouponCustomerService extends IService<DiscountCouponCustomer> {

    Result<List<DiscountCouponCustomer>> discountCouponCustomerList(Integer state);
}
