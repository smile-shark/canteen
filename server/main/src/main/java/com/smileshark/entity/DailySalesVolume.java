package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜品每日销售数量
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("daily_sales_volume")
public class DailySalesVolume {

    /**
     * 对应的菜品
     */
    @TableField("cuisine_id")
    private String cuisineId;

    /**
     * 销售的日期
     */
    @TableField("sales_date")
    private LocalDate salesDate;

    /**
     * 每日的销售数量
     */
    @TableField("num")
    private Long num;
}
