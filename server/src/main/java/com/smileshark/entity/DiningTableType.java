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
 * 餐桌类型
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dining_table_type")
public class DiningTableType {

    @TableId(value = "dining_table_type_id", type = IdType.ASSIGN_UUID)
    private String diningTableTypeId;

    /**
     * 桌位类型(0：小桌、1：大桌、2：包间)
     */
    @TableField("type")
    private Integer type;

    /**
     * 最多人数
     */
    @TableField("max")
    private Integer max;

    /**
     * 最少人数
     */
    @TableField("min")
    private Integer min;
}
