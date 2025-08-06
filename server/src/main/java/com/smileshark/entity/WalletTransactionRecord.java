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
 * 钱包交易记录
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("wallet_transaction_record")
public class WalletTransactionRecord {

    @TableId(value = "wallet_transaction_record_id", type = IdType.ASSIGN_UUID)
    private String walletTransactionRecordId;

    /**
     * 交易记录对应的钱包
     */
    @TableField("wallet_id")
    private String walletId;

    /**
     * 交易类型(0：储值、1：消费)
     */
    @TableField("type")
    private Integer type;

    /**
     * 交易金额
     */
    @TableField("balance")
    private Double balance;

    /**
     * 交易方式，后面两个是消费时的(0：支付宝支付、1：微信支付、2：现金支付、3：银行卡、0：外卖、1：门店)
     */
    @TableField("record_fun")
    private Integer recordFun;
}
