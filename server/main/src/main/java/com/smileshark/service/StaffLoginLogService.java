package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.StaffLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 * 员工登录日志 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface StaffLoginLogService extends IService<StaffLoginLog> {

    Result<Page<StaffLoginLog>> pageList(Integer page, Integer size, String shopId, LocalDate startDate, LocalDate endDate);
}
