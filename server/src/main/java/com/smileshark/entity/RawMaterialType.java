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
 * 原料分类表
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("raw_material_type")
public class RawMaterialType {

    @TableId(value = "raw_material_type_id", type = IdType.ASSIGN_UUID)
    private String rawMaterialTypeId;

    /**
     * 分类的名称
     */
    @TableField("name")
    private String name;

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
}
