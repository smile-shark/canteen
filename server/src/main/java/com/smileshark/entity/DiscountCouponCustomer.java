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
 * 用户拥有的优惠券，优惠券和客户的中间表
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("discount_coupon_customer")
public class DiscountCouponCustomer {

    /**
     * 一个客户可能有多个同总的优惠券，使用的时候不能直接使用优惠券的发行id
     */
    @TableId(value = "discount_coupon_customer_id", type = IdType.ASSIGN_UUID)
    private String discountCouponCustomerId;

    /**
     * 所属的客户
     */
    @TableField("customer_id")
    private String customerId;

    /**
     * 如果是堂食收银收的，如果要使用就需要这个id
     */
    @TableField("staff_id")
    private String staffId;

    /**
     * 对应的优惠券
     */
    @TableField("discount_coupon_id")
    private String discountCouponId;

    /**
     * 客户获取优惠券的时间
     */
    @TableField("get_time")
    private LocalDateTime getTime;

    /**
     * 优惠券的使用时间
     */
    @TableField("use_time")
    private LocalDateTime useTime;

    /**
     * 优惠券状体(0：未使用、1：已使用、2：已过期)
     */
    @TableField("state")
    private Integer state;
}
