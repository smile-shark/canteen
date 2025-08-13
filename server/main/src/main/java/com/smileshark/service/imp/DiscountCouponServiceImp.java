package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.*;
import com.smileshark.mapper.*;
import com.smileshark.service.DiscountCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.InfoThreadLocal;
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
    private final DiscountCouponMapper discountCouponMapper;
    private final CuisineMapper cuisineMapper;

    @Override
    public Result<Page<DiscountCoupon>> pageList(Integer page, Integer size, Integer type, String shopId) {
        LambdaQueryChainWrapper<DiscountCoupon> query = lambdaQuery();
        if (type != null) {
            query.eq(DiscountCoupon::getType, type);
        }
        if (shopId != null && !shopId.isEmpty()) {
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
        if (discountCoupon.getShopId().isEmpty()) {
            discountCoupon.setShopId(null);
        }
        discountCoupon.setSurplusNum(discountCoupon.getGrantNum());
        discountCoupon.setDiscountCouponId(discountCouponId);
        save(discountCoupon);
        // 判断优惠券的使用范围
        if (discountCoupon.getUseArea() == 1) {
            // 存入对应的菜品范围
            for (DiscountCouponAreaCuisine discountCouponAreaCuisine : discountCoupon.getDiscountCouponAreaCuisines()) {
                discountCouponAreaCuisine.setDiscountCouponId(discountCouponId);
                discountCouponAreaCuisineMapper.insert(discountCouponAreaCuisine);
            }
        } else if (discountCoupon.getUseArea() == 2) {
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
        return Result.success(lambdaQuery().gt(DiscountCoupon::getEndTime, new Date()).gt(DiscountCoupon::getSurplusNum, 0).list());
    }

    @Override
    @Transactional
    public Result<?> sendCoupon(String couponId, String customerId) {
        // 1. 校验优惠券
        DiscountCoupon one = lambdaQuery().eq(DiscountCoupon::getDiscountCouponId, couponId).one();
        if (one == null) {
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

    @Override
    public Result<List<DiscountCouponCustomer>> usableCoupon(String shopId, List<String> cuisineIds, Double price) {

        // 先查找到该用户可用的优惠券
        List<DiscountCouponCustomer> discountCouponCustomers = discountCouponCustomerMapper.selectList(new LambdaQueryWrapper<>(DiscountCouponCustomer.class).eq(DiscountCouponCustomer::getCustomerId, InfoThreadLocal.getCustomer().getCustomerId()).eq(DiscountCouponCustomer::getState, 0));
        for (DiscountCouponCustomer discountCouponCustomer : discountCouponCustomers) {
            discountCouponCustomer.setDiscountCoupon(discountCouponMapper.selectById(discountCouponCustomer.getDiscountCouponId()));
        }
        // 再过滤出符合条件的优惠券
        discountCouponCustomers = discountCouponCustomers.stream().filter(discountCouponCustomer -> {
            // 1. 过滤店铺，找出对应店铺id的和没有shopId的优惠券
            if (!(discountCouponCustomer.getDiscountCoupon().getShopId() == null || discountCouponCustomer.getDiscountCoupon().getShopId().equals(shopId) || discountCouponCustomer.getDiscountCoupon().getShopId().isEmpty())) {
                return false;
            }
            // 2. 根据订单的金额进行筛选
            if (discountCouponCustomer.getDiscountCoupon().getCondition() > price) {
                return false;
            }
            // 3. 根据菜品的使用范围进行筛选
            switch (discountCouponCustomer.getDiscountCoupon().getUseArea()) {
                case 1:
                    // 获取对应的菜品范围，查看是否包含传递的菜品列表
                    LambdaQueryWrapper<DiscountCouponAreaCuisine> queryWrapper = new LambdaQueryWrapper<>(DiscountCouponAreaCuisine.class).eq(DiscountCouponAreaCuisine::getDiscountCouponId, discountCouponCustomer.getDiscountCouponId());
                    if (cuisineIds != null && !cuisineIds.isEmpty()) {
                        queryWrapper.in(DiscountCouponAreaCuisine::getCuisineId, cuisineIds);
                    }
                    List<DiscountCouponAreaCuisine> discountCouponAreaCuisines = discountCouponAreaCuisineMapper.selectList(queryWrapper);
                    if (discountCouponAreaCuisines.isEmpty()) {
                        return false;
                    }
                    break;
                case 2:
                    // 获取到优惠券的分类使用范围
                    List<String> cuisineTypeIds = discountCouponAreaCuisineTypeMapper.selectList(new LambdaQueryWrapper<>(DiscountCouponAreaCuisineType.class).eq(DiscountCouponAreaCuisineType::getDiscountCouponId, discountCouponCustomer.getDiscountCouponId())).stream().map(DiscountCouponAreaCuisineType::getCuisineTypeId).toList();
                    // 获取菜品的分类范围
                    LambdaQueryWrapper<Cuisine> cuisineLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    if (cuisineIds != null && !cuisineIds.isEmpty()) {
                        cuisineLambdaQueryWrapper.in(Cuisine::getCuisineId, cuisineIds);
                    }
                    if (!cuisineTypeIds.isEmpty()) {
                        cuisineLambdaQueryWrapper.in(Cuisine::getCuisineTypeId, cuisineTypeIds);
                    }
                    List<Cuisine> cuisines = cuisineMapper.selectList(cuisineLambdaQueryWrapper);
                    if (cuisines.isEmpty()) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
            return true;
        }).toList();
        // 2. 根据优惠券的使用范围进行筛选
        return Result.success(discountCouponCustomers);
    }
}
