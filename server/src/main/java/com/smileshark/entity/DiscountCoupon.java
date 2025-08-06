package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("discount_coupon")
public class DiscountCoupon {

    @TableId(value = "discount_coupon_id", type = IdType.ASSIGN_UUID)
    private String discountCouponId;

    /**
     * 有效期开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 有效期结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 优惠券的名称
     */
    @TableField("name")
    private String name;

    /**
     * 优惠券的类型(0：满减券、1：折扣券)
     */
    @TableField("type")
    private Integer type;

    /**
     * 优惠券金额
     */
    @TableField("price")
    private Double price;

    /**
     * 优惠的折扣数(0-10)
     */
    @TableField("discount")
    private Double discount;

    /**
     * 满多少可用使用的门槛
     */
    @TableField("condition")
    private Double condition;

    /**
     * 发放的数量
     */
    @TableField("grant_num")
    private byte[] grantNum;

    /**
     * 剩余的数量
     */
    @TableField("surplus_num")
    private byte[] surplusNum;

    /**
     * 优惠券的使用量，有些可能领取了但是没有被使用
     */
    @TableField("use_num")
    private byte[] useNum;

    /**
     * 每人限制领取的数量
     */
    @TableField("astrict_num")
    private byte[] astrictNum;

    /**
     * 所属的门店
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 可用使用的范围(0：全部菜品、1：指定菜品、3：指定分类)
     */
    @TableField("use_area")
    private Integer useArea;
}
