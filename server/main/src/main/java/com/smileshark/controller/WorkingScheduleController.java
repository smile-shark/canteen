package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Staff;
import com.smileshark.entity.WorkingSchedule;
import com.smileshark.service.WorkingScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 排班表 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/workingSchedule")
@RequiredArgsConstructor
public class WorkingScheduleController {
    private final WorkingScheduleService workingScheduleService;
    @GetMapping("/pageList")
    public Result<Page<Staff>> pageList(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer size, @RequestParam(required = false) String shopId, @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate){
        System.out.println("startDate = " + startDate);
        return workingScheduleService.pageList(page, size, shopId, startDate, endDate);
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody WorkingSchedule workingSchedule){
        System.out.println("workingSchedule = " + workingSchedule);
        return workingScheduleService.add(workingSchedule);
    }
}
