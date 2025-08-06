package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券使用范围的菜品类型
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("discount_coupon_area_cuisine_type")
public class DiscountCouponAreaCuisineType {

    /**
     * 所属的优惠券
     */
    @TableField("discount_coupon_id")
    private String discountCouponId;

    /**
     * 对应的菜品类别
     */
    @TableField("cuisine_type_id")
    private String cuisineTypeId;

    /**
     * 展示时的排序
     */
    @TableField("show_order")
    private Integer showOrder;
}
