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
 * 菜品的分量/规格
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cuisine_component")
public class CuisineComponent {

    @TableId(value = "cuisine_component_id", type = IdType.ASSIGN_UUID)
    private String cuisineComponentId;

    /**
     * 分量的名字
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
