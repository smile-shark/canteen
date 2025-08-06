package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退款申请的照片
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("drawback_application_image")
public class DrawbackApplicationImage {

    /**
     * 被退款的订单
     */
    @TableField("customer_order_id")
    private String customerOrderId;

    /**
     * 图片的连接
     */
    @TableField("url")
    private String url;

    /**
     * 展示的排序
     */
    @TableField("show_order")
    private Integer showOrder;
}
