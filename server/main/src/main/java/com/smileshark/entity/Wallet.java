package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 钱包
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("wallet")
public class Wallet {

    @TableId(value = "wallet_id", type = IdType.ASSIGN_UUID)
    private String walletId;

    /**
     * 余额
     */
    @TableField("balance")
    private Double balance;

    /**
     * 支付密码，使用MD5加盐加密
     */
    @TableField("pay_password")
    private String payPassword;
}
