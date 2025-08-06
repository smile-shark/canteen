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
 * 评价
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("evaluate")
public class Evaluate {

    @TableId(value = "evaluate_id", type = IdType.ASSIGN_UUID)
    private String evaluateId;

    /**
     * 客户订单id
     */
    @TableField("customer_order_id")
    private String customerOrderId;

    /**
     * 评价分数(1-5)对应星数
     */
    @TableField("score")
    private Integer score;

    /**
     * 评价的内容
     */
    @TableField("content")
    private String content;

    /**
     * 评价的时间
     */
    @TableField("time")
    private LocalDateTime time;
}
