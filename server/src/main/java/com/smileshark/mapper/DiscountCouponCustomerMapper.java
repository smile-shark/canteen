package com.smileshark.mapper;

import com.smileshark.entity.DiscountCouponCustomer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户拥有的优惠券，优惠券和客户的中间表 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Mapper
public interface DiscountCouponCustomerMapper extends BaseMapper<DiscountCouponCustomer> {

}
