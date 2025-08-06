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
 * 餐桌
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dining_table")
public class DiningTable {

    @TableId(value = "dining_table_id", type = IdType.ASSIGN_UUID)
    private String diningTableId;

    /**
     * 编号（1F 10010号）
     */
    @TableField("serial_number")
    private String serialNumber;

    /**
     * 餐桌的类型
     */
    @TableField("dining_table_type_id")
    private String diningTableTypeId;

    /**
     * 餐桌的状态(0：空闲、1：待点菜、2：预结账、3：用餐中)
     */
    @TableField("user_state")
    private Integer userState;

    /**
     * 所属的门店
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 启用状态(0：启用、1：停用)
     */
    @TableField("state")
    private Integer state;

    /**
     * 台桌点餐的二维码，生成后这里存储url，或者可用尝试其他类型的数据
     */
    @TableField("qr_code")
    private String qrCode;
}
