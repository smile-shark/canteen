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
 * 客户订单
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("customer_order")
public class CustomerOrder {

    /**
     * 订单id yyyyMMdd+UUID32
     */
    @TableId(value = "customer_order_id", type = IdType.ASSIGN_UUID)
    private String customerOrderId;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private String customerId;

    /**
     * 如果是服务员点的就需要这个id
     */
    @TableField("staff_id")
    private String staffId;

    /**
     * 订单总额
     */
    @TableField("all_price")
    private Double allPrice;

    /**
     * 订单状态(0：待付款、1：待收取、2：已完成、3：已取消、4：待评价、5：未处理、6：已接单、7：已拒绝、8：已送达、9：已自提、10：已作废、11：已退款)
     */
    @TableField("state")
    private Integer state;

    /**
     * 打包费
     */
    @TableField("packing_charges")
    private Double packingCharges;

    /**
     * 配送费
     */
    @TableField("delivery_cost")
    private Double deliveryCost;

    /**
     * 使用的优惠券
     */
    @TableField("discount_coupon_customer_id")
    private String discountCouponCustomerId;

    /**
     * 餐具数量
     */
    @TableField("tableware_num")
    private Integer tablewareNum;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private LocalDateTime payTime;

    /**
     * 订单来源(0：门店、1：线上)
     */
    @TableField("source")
    private Integer source;

    /**
     * 订单类型(0：堂食、1：外卖、2：自取)
     */
    @TableField("type")
    private Integer type;

    @TableField("dining_table_id")
    private String diningTableId;

    @TableField("delivery_address_id")
    private String deliveryAddressId;

    /**
     * 支付方式(0：微信支付、1：支付宝支付、2：现金支付、3：银行卡支付、4：会员卡支付)
     */
    @TableField("pay_type")
    private Integer payType;

    /**
     * 是否被用户删除(0：未删除、1：已删除)
     */
    @TableField("delete")
    private Integer delete;
}
