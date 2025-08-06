package com.smileshark.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Staff;
import com.smileshark.entity.WorkingSchedule;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.StaffMapper;
import com.smileshark.mapper.WorkingScheduleMapper;
import com.smileshark.service.WorkingScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 * 排班表 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class WorkingScheduleServiceImp extends ServiceImpl<WorkingScheduleMapper, WorkingSchedule> implements WorkingScheduleService {
    private final StaffMapper staffMapper;

    @Override
    public Result<Page<Staff>> pageList(Integer page, Integer size, String shopId, LocalDate startDate, LocalDate endDate) {
        // 先分页查询到员工的列表
        Page<Staff> staffPage = staffMapper.selectStaffPageList(new Page<>(page, size), null, shopId, 0);
        // 再根据员工的id查询到排班信息
        for (Staff staff : staffPage.getRecords()) {
            staff.setWorkingSchedules(
                    lambdaQuery().eq(WorkingSchedule::getStaffId, staff.getStaffId())
                            .between(WorkingSchedule::getWorkingDate, startDate, endDate)
                            .list()
            );
        }
        return Result.success(staffPage);
    }

    @Override
    public Result<?> add(WorkingSchedule workingSchedule) {
        // 判断这个员工在这个时间点是否已经有排班了
        WorkingSchedule oldWorkingSchedule = lambdaQuery().eq(WorkingSchedule::getStaffId, workingSchedule.getStaffId()).eq(WorkingSchedule::getWorkingDate, workingSchedule.getWorkingDate()).one();
        if (oldWorkingSchedule== null) {
            // 没有就进行添加
            if (!save(workingSchedule)) {
                throw new BusinessException(ResultCode.ADD_ERROR);
            }
        }else{
            // 否则就更新
            if (!update(workingSchedule,new LambdaQueryWrapper<>(WorkingSchedule.class).eq(WorkingSchedule::getStaffId, workingSchedule.getStaffId()).eq(WorkingSchedule::getWorkingDate, workingSchedule.getWorkingDate()))) {
                throw new BusinessException(ResultCode.UPDATE_ERROR);
            }
        }
        return Result.success(ResultCode.ADD_SUCCESS);
    }
}
