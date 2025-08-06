package com.smileshark.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 排班表
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Data
@Accessors(chain = true)
@TableName("working_schedule")
public class WorkingSchedule {

    /**
     * 排班对应的员工
     */
    @TableField("staff_id")
    private String staffId;

    /**
     * 日期
     */
    @TableField("working_date")
    private LocalDate workingDate;

    /**
     * 排班的类型(0：早班、1：晚班、2：休)
     */
    @TableField("type")
    private Integer type;
}
