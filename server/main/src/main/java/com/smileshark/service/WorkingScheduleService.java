package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Staff;
import com.smileshark.entity.WorkingSchedule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 * 排班表 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface WorkingScheduleService extends IService<WorkingSchedule> {

    Result<Page<Staff>> pageList(Integer page, Integer size, String shopId, LocalDate startDate, LocalDate endDate);

    Result<?> add(WorkingSchedule workingSchedule);
}
