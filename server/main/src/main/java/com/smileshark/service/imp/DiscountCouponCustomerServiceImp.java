package com.smileshark.service.imp;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCouponCustomer;
import com.smileshark.mapper.DiscountCouponCustomerMapper;
import com.smileshark.mapper.DiscountCouponMapper;
import com.smileshark.service.DiscountCouponCustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.InfoThreadLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户拥有的优惠券，优惠券和客户的中间表 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class DiscountCouponCustomerServiceImp extends ServiceImpl<DiscountCouponCustomerMapper, DiscountCouponCustomer> implements DiscountCouponCustomerService {
    private final DiscountCouponCustomerMapper discountCouponCustomerMapper;
    private final DiscountCouponMapper discountCouponMapper;

    @Override
    public Result<List<DiscountCouponCustomer>> discountCouponCustomerList(Integer state) {
        return Result.success(discountCouponCustomerMapper.discountCouponCustomerList(InfoThreadLocal.getCustomer().getCustomerId(), state));
    }
}
