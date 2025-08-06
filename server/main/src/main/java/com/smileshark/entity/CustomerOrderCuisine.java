package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单菜品列表
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("customer_order_cuisine")
public class CustomerOrderCuisine {

    /**
     * 客户订单id
     */
    @TableField("customer_order_id")
    private String customerOrderId;

    @TableField("cuisine_id")
    private String cuisineId;

    /**
     * 当前菜品的数量
     */
    @TableField("num")
    private Integer num;

    /**
     * 菜品的排序
     */
    @TableField("cuisine_order")
    private Integer cuisineOrder;
}
