package com.smileshark.service.imp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.StaffLoginLog;
import com.smileshark.mapper.StaffLoginLogMapper;
import com.smileshark.service.StaffLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 * 员工登录日志 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class StaffLoginLogServiceImp extends ServiceImpl<StaffLoginLogMapper, StaffLoginLog> implements StaffLoginLogService {
    private final StaffLoginLogMapper staffLoginLogMapper;

    @Override
    public Result<Page<StaffLoginLog>> pageList(Integer page, Integer size, String shopId, LocalDate startDate, LocalDate endDate) {
        return Result.success(staffLoginLogMapper.pageList(new Page<>(page, size), shopId, startDate,endDate));
    }
}
