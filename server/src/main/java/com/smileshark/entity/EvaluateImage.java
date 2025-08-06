package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 评价图片
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("evaluate_image")
public class EvaluateImage {

    /**
     * 评价id
     */
    @TableField("evaluate_id")
    private String evaluateId;

    /**
     * 图片连接
     */
    @TableField("url")
    private String url;

    /**
     * 图片的排序
     */
    @TableField("image_order")
    private Integer imageOrder;
}
