package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单菜品列表
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("customer_order_cuisine")
public class CustomerOrderCuisine {

    public CustomerOrderCuisine(String customerOrderId, String cuisineId, Integer num, Integer cuisineOrder) {
        this.customerOrderId = customerOrderId;
        this.cuisineId = cuisineId;
        this.num = num;
        this.cuisineOrder = cuisineOrder;
    }

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

    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String cuisineNum ;
    @TableField(exist = false)
    private Double price;

}
