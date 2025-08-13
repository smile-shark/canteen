package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.DiscountCoupon;
import com.smileshark.entity.DiscountCouponAreaCuisine;
import com.smileshark.entity.DiscountCouponAreaCuisineType;
import com.smileshark.entity.DiscountCouponCustomer;
import com.smileshark.mapper.DiscountCouponAreaCuisineMapper;
import com.smileshark.mapper.DiscountCouponAreaCuisineTypeMapper;
import com.smileshark.mapper.DiscountCouponCustomerMapper;
import com.smileshark.mapper.DiscountCouponMapper;
import com.smileshark.service.DiscountCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
    private final DiscountCouponAreaCuisineMapper discountCouponAreaCuisineMapper;
    private final DiscountCouponAreaCuisineTypeMapper discountCouponAreaCuisineTypeMapper;
    private final DiscountCouponCustomerMapper discountCouponCustomerMapper;

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

    @Override
    @Transactional
    public Result<?> add(DiscountCoupon discountCoupon) {
        String discountCouponId = IdUtil.simpleUUID();
        // 处理字段防止报错
        if(discountCoupon.getShopId().isEmpty()){
            discountCoupon.setShopId(null);
        }
        discountCoupon.setSurplusNum(discountCoupon.getGrantNum());
        discountCoupon.setDiscountCouponId(discountCouponId);
        save(discountCoupon);
        // 判断优惠券的使用范围
        if(discountCoupon.getUseArea()==1){
            // 存入对应的菜品范围
            for (DiscountCouponAreaCuisine discountCouponAreaCuisine : discountCoupon.getDiscountCouponAreaCuisines()) {
                discountCouponAreaCuisine.setDiscountCouponId(discountCouponId);
                discountCouponAreaCuisineMapper.insert(discountCouponAreaCuisine);
            }
        }else if(discountCoupon.getUseArea()==2){
            // 存入对应的菜品分类范围
            for (DiscountCouponAreaCuisineType discountCouponAreaCuisineType : discountCoupon.getDiscountCouponAreaCuisineTypes()) {
                discountCouponAreaCuisineType.setDiscountCouponId(discountCouponId);
                discountCouponAreaCuisineTypeMapper.insert(discountCouponAreaCuisineType);
            }
        }
        return Result.success(ResultCode.ADD_SUCCESS);
    }

    @Override
    public Result<List<DiscountCoupon>> sendCouponList() {
        return Result.success(lambdaQuery().gt(DiscountCoupon::getEndTime,new Date()).gt(DiscountCoupon::getSurplusNum,0).list());
    }

    @Override
    @Transactional
    public Result<?> sendCoupon(String couponId, String customerId) {
        // 1. 校验优惠券
        DiscountCoupon one = lambdaQuery().eq(DiscountCoupon::getDiscountCouponId, couponId).one();
        if(one == null){
            return Result.error(ResultCode.COUPON_NOT_EXIST);
        }
        if (one.getEndTime().isBefore(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
            return Result.error(ResultCode.COUPON_EXPIRED);
        }
        if (one.getSurplusNum() <= 0) {
            return Result.error(ResultCode.COUPON_NOT_ENOUGH);
        }
        // 2. 更新优惠券数据
        one.setSurplusNum(one.getSurplusNum() - 1);
        updateById(one);
        // 3. 添加优惠券到用户中间表
        DiscountCouponCustomer discountCouponCustomer = new DiscountCouponCustomer();
        discountCouponCustomer.setDiscountCouponCustomerId(IdUtil.simpleUUID());
        discountCouponCustomer.setCustomerId(customerId);
        discountCouponCustomer.setDiscountCouponId(couponId);
        discountCouponCustomerMapper.insert(discountCouponCustomer);

        return Result.success(ResultCode.SEND_COUPON_SUCCESS);
    }
}
