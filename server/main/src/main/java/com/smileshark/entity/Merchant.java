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
 * 商户
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("merchant")
public class Merchant {

    /**
     * 商户id
     */
    @TableId(value = "merchant_id", type = IdType.ASSIGN_UUID)
    private String merchantId;

    /**
     * 商户的账号
     */
    @TableField("account")
    private String account;

    /**
     * 商户账号的密码
     */
    @TableField("password")
    private String password;
}
