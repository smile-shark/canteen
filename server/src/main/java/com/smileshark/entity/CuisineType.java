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
 * 菜品的分类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cuisine_type")
public class CuisineType {

    @TableId(value = "cuisine_type_id", type = IdType.ASSIGN_UUID)
    private String cuisineTypeId;

    /**
     * 分类的名称(招牌菜、热菜...)
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
