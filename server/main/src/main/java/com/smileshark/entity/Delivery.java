package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配送的配置有点多，就分开了
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("delivery")
public class Delivery {

    /**
     * 对应的门店
     */
    @TableId(value = "shop_id", type = IdType.INPUT)
    @TableField("shop_id")
    private String shopId;

    /**
     * 外卖/自提时间开始
     */
    @TableField("delivery_time_start")
    private LocalTime deliveryTimeStart;

    /**
     * 外卖/自提时间结束
     */
    @TableField("delivery_time_end")
    private LocalTime deliveryTimeEnd;

    /**
     * 是否开始满免配送费
     */
    @TableField("is_full_free_delivery")
    private Integer isFullFreeDelivery;

    /**
     * 满多少元免费配送
     */
    @TableField("full_free_delivery_price")
    private Double fullFreeDeliveryPrice;

    /**
     * 配送费收取方式(0：固定区域收费、1：按距离阶梯收费)
     */
    @TableField("payment_method")
    private Integer paymentMethod;

    /**
     * 设置区域半径
     */
    @TableField("area")
    private Double area;

    /**
     * 设置区域固定费用
     */
    @TableField("area_delivery_price")
    private Double areaDeliveryPrice;

    /**
     * 初始收费公里数
     */
    @TableField("distance_start_num")
    private Double distanceStartNum;

    /**
     * 初始收费公价格
     */
    @TableField("distance_start_price")
    private Double distanceStartPrice;

    /**
     * 没增加的公里数
     */
    @TableField("distance_add_num")
    private Double distanceAddNum;

    /**
     * 每次增加的费用
     */
    @TableField("distance_add_price")
    private Double distanceAddPrice;
}
