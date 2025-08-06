package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 后厨订单
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("back_kitchen_order")
public class BackKitchenOrder {

    @TableId(value = "back_kitchen_order_id", type = IdType.ASSIGN_UUID)
    private String backKitchenOrderId;

    /**
     * 对用的客户订单，其中包含了订单的所有数据
     */
    @TableField("customer_order_id")
    private String customerOrderId;

    /**
     * 创建时间（这个时间决定后厨是否该出餐）
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
