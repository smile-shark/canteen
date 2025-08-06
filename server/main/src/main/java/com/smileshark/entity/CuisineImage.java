package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜品的图片，一个菜可能有多个图片
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("cuisine_image")
public class CuisineImage {

    /**
     * 菜品id
     */
    @TableField("cuisine_id")
    private String cuisineId;

    /**
     * 图片的url
     */
    @TableField("url")
    private String url;

    /**
     * 图片的排序
     */
    @TableField("image_order")
    private Integer imageOrder;
}
