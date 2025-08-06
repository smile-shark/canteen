package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 小票的样式管理
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("receipt")
public class Receipt {

    /**
     * 小票的类型(0：堂食小票、1：外卖小票、2：后厨小票、3：充值小票)
     */
    @TableField("type")
    private Integer type;

    /**
     * 门店名称(0：不显示、1显示)---可用的小票类型(013)
     */
    @TableField("shop_name")
    private Integer shopName;

    /**
     * 头部欢迎语(0：不显示、1显示)---可用的小票类型(013)
     */
    @TableField("title_welecome")
    private Integer titleWelecome;

    /**
     * 头部欢迎内容
     */
    @TableField("title_welecome_word")
    private String titleWelecomeWord;

    /**
     * 票据类型(0：不显示、1显示)---可用的小票类型(0123)
     */
    @TableField("receipt_type")
    private Integer receiptType;

    /**
     * 桌号/取餐号(0：不显示、1显示)---可用的小票类型(02)
     */
    @TableField("table_num")
    private Integer tableNum;

    /**
     * 人数(0：不显示、1显示)---可用的小票类型(0)
     */
    @TableField("people_num")
    private Integer peopleNum;

    /**
     * 订单编号(0：不显示、1显示)---可用的小票类型(0123)
     */
    @TableField("oreder_num")
    private Integer orederNum;

    /**
     * 下单时间(0：不显示、1显示)---可用的小票类型(012)
     */
    @TableField("oreder_create_time")
    private Integer orederCreateTime;

    /**
     * 菜品详情(0：不显示、1显示)---可用的小票类型(012)
     */
    @TableField("cuisine_detail")
    private Integer cuisineDetail;

    /**
     * 备注(0：不显示、1显示)---可用的小票类型(02)
     */
    @TableField("remark")
    private Integer remark;

    /**
     * 支付详情(0：不显示、1显示)---可用的小票类型(01)
     */
    @TableField("pay_detail")
    private Integer payDetail;

    /**
     * 优惠详情(0：不显示、1显示)---可用的小票类型(01)
     */
    @TableField("discount_detail")
    private Integer discountDetail;

    /**
     * 门店地址(0：不显示、1显示)---可用的小票类型(013)
     */
    @TableField("shop_address")
    private Integer shopAddress;

    /**
     * 门店电话(0：不显示、1显示)---可用的小票类型(013)
     */
    @TableField("shop_phone")
    private Integer shopPhone;

    /**
     * 公众号二维码(0：不显示、1显示)---可用的小票类型(013)
     */
    @TableField("wechat_public_account_qr_code")
    private Integer wechatPublicAccountQrCode;

    /**
     * 小程序二维码(0：不显示、1显示)---可用的小票类型(013)
     */
    @TableField("mini_program_qr_code")
    private Integer miniProgramQrCode;

    /**
     * 结束致谢语(0：不显示、1显示)---可用的小票类型(01)
     */
    @TableField("closing_remarks_gratitude")
    private Integer closingRemarksGratitude;

    /**
     * 结束致谢语内容
     */
    @TableField("closing_remarks_gratitude_word")
    private Integer closingRemarksGratitudeWord;

    /**
     * 顾客姓名(0：不显示、1显示)---可用的小票类型(13)
     */
    @TableField("customer_name")
    private Integer customerName;

    /**
     * 顾客手机号(0：不显示、1显示)---可用的小票类型(13)
     */
    @TableField("customer_phone")
    private Integer customerPhone;

    /**
     * 配送地址(0：不显示、1显示)---可用的小票类型(1)
     */
    @TableField("customer_address")
    private Integer customerAddress;

    /**
     * 配送时间/自提时间(0：不显示、1显示)---可用的小票类型(12)
     */
    @TableField("take_out_time")
    private Integer takeOutTime;

    /**
     * 餐盒费(0：不显示、1显示)---可用的小票类型(1)
     */
    @TableField("food_container_cost")
    private Integer foodContainerCost;

    /**
     * 配送费(0：不显示、1显示)---可用的小票类型(1)
     */
    @TableField("delivery_cost")
    private Integer deliveryCost;

    /**
     * 充值时间(0：不显示、1显示)---可用的小票类型(3)
     */
    @TableField("recharge_time")
    private Integer rechargeTime;

    /**
     * 充值详情(0：不显示、1显示)---可用的小票类型(3)
     */
    @TableField("recharge_detail")
    private Integer rechargeDetail;
}
