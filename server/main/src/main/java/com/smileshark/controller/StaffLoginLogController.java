package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.StaffLoginLog;
import com.smileshark.service.StaffLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * <p>
 * 员工登录日志 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/staffLoginLog")
@RequiredArgsConstructor
public class StaffLoginLogController {
    private final StaffLoginLogService staffLoginLogService;
    @GetMapping("/pageList")
   public Result<Page<StaffLoginLog>> pageList(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer size, @RequestParam(required = false)String shopId, @RequestParam(required = false)LocalDate startDate, @RequestParam(required = false)LocalDate endDate){
        return staffLoginLogService.pageList(page, size, shopId, startDate, endDate);
    }
}
