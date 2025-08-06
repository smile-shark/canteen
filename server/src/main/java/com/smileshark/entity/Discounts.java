package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 除开优惠券的其他优惠
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("discounts")
public class Discounts {

    /**
     * 优惠的客户订单
     */
    @TableField("customer_order_id")
    private String customerOrderId;

    /**
     * 优惠的方式(0：打折、1：免单、2：会员优惠、3：满减优惠)
     */
    @TableField("type")
    private Integer type;

    /**
     * 折扣值:(0-10)，如果免单就填0
     */
    @TableField("num")
    private Double num;

    /**
     * 优惠的金额
     */
    @TableField("balance")
    private Double balance;

    /**
     * 优惠原因
     */
    @TableField("content")
    private String content;
}
