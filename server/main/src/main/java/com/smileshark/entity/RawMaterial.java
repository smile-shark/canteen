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
 * 原料表
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("raw_material")
public class RawMaterial {

    @TableId(value = "raw_material_id", type = IdType.ASSIGN_UUID)
    private String rawMaterialId;

    /**
     * 原料对应的分类
     */
    @TableField("raw_material_type_id")
    private String rawMaterialTypeId;

    /**
     * 原料的名称
     */
    @TableField("name")
    private String name;

    /**
     * 所属的门店
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 序号(0001)
     */
    @TableField("num")
    private String num;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 库存数量
     */
    @TableField("inventory")
    private byte[] inventory;

    /**
     * 库存过低预警数量
     */
    @TableField("warning_min")
    private byte[] warningMin;

    /**
     * 库存过高预警数量
     */
    @TableField("warning_max")
    private byte[] warningMax;
}
