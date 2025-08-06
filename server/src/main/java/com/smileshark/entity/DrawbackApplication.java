package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退款申请
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("drawback_application")
public class DrawbackApplication {

    /**
     * 被退款的订单
     */
    @TableId(value = "customer_order_id", type = IdType.ASSIGN_UUID)
    private String customerOrderId;

    /**
     * 申请的状态(0：未处理、1：已驳回、2：已处理)
     */
    @TableField("state")
    private Integer state;

    /**
     * 退款的原因
     */
    @TableField("drawback_reason")
    private String drawbackReason;

    /**
     * 驳回的原因
     */
    @TableField("rejeck_reason")
    private String rejeckReason;

    /**
     * 退款的金额，理论不能超过订单
     */
    @TableField("balance")
    private Double balance;

    /**
     * 处理人
     */
    @TableField("staff_id")
    private String staffId;
}
