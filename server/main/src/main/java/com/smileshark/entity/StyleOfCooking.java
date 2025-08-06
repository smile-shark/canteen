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
 * 菜系
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("style_of_cooking")
public class StyleOfCooking {

    @TableId(value = "style_of_cooking_id", type = IdType.ASSIGN_UUID)
    private String styleOfCookingId;

    /**
     * 菜系的名字
     */
    @TableField("name")
    private String name;
}
