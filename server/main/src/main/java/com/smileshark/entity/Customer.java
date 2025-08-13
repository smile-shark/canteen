package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Data
@Accessors(chain = true)
@TableName("customer")
public class Customer {

    /**
     * 客户id
     */
    @TableId(value = "customer_id", type = IdType.ASSIGN_UUID)
    private String customerId;

    /**
     * 积分
     */
    @TableField("integral")
    private Integer integral;

    /**
     * 客户对用的钱包
     */
    @TableField("wallet_id")
    private String walletId;

    /**
     * 头像
     */
    @TableField("head_portrait")
    private String headPortrait;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 会员等级(0：青铜、1：白银、2：黄金...)
     */
    @TableField("level")
    private Integer level;

    /**
     * 消费总额(可用根据这个消费升级会员等级)
     */
    @TableField("consumption_plance")
    private Double consumptionPlance;

    /**
     * 消费的总次数
     */
    @TableField("consumption_num")
    private Long consumptionNum;

    /**
     * 默认的收货地址
     */
    @TableField("delivery_address_id")
    private String deliveryAddressId;

    /**
     * 绑定的手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 性别(0：男、1：女、2：保密)
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 用户的生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建的门店，可用这个来区分门店的新人绩效
     */
    @TableField("shop_id")
    private String shopId;

    @TableField(exist = false)
    private Wallet wallet;
}
