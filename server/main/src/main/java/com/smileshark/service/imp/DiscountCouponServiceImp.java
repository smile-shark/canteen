package com.smileshark.service.imp;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCoupon;
import com.smileshark.mapper.DiscountCouponMapper;
import com.smileshark.service.DiscountCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class DiscountCouponServiceImp extends ServiceImpl<DiscountCouponMapper, DiscountCoupon> implements DiscountCouponService {

    @Override
    public Result<Page<DiscountCoupon>> pageList(Integer page, Integer size, Integer type, String shopId) {
        LambdaQueryChainWrapper<DiscountCoupon> query = lambdaQuery();
        if(type!= null){
            query.eq(DiscountCoupon::getType, type);
        }
        if(shopId!= null&& !shopId.isEmpty()){
            query.eq(DiscountCoupon::getShopId, shopId);
        }
        query.last("order by end_time desc");
        return Result.success(query.page(new Page<>(page, size)));
    }
}
