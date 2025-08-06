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
 * 打印机
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("printer")
public class Printer {

    @TableId(value = "printer_id", type = IdType.ASSIGN_UUID)
    private String printerId;

    /**
     * 打印机的编号 (00001)
     */
    @TableField("num")
    private String num;

    /**
     * 设备的名称 (惠普DJ2720)
     */
    @TableField("name")
    private String name;

    /**
     * 打印机的类型(0：云打印机、1：网口打印机、2：USB打印机)
     */
    @TableField("type")
    private Integer type;

    /**
     * 小票类型(0：普通小票、1：标签小票)
     */
    @TableField("receipt_type")
    private Integer receiptType;

    /**
     * 设备的品牌
     */
    @TableField("brand")
    private String brand;

    /**
     * 设备的SN号
     */
    @TableField("sn_num")
    private String snNum;

    /**
     * 设备key
     */
    @TableField("printer_key")
    private String printerKey;

    /**
     * 设备规格(0：80mm、1：58mm)
     */
    @TableField("receipt_specification")
    private Integer receiptSpecification;

    /**
     * 打印机所属的门店
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 状态(0：可用、1：不可用)
     */
    @TableField("state")
    private Integer state;
}
