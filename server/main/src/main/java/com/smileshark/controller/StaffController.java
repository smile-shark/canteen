package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Staff;
import com.smileshark.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 员工 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @GetMapping("/pageList")
    public Result<Page<Staff>> pageList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String num, // 姓名/编号/联系方式
            @RequestParam(required = false) String shopId,
            @RequestParam(required = false) Integer state
    ) {
        return staffService.pageList(page, size, num, shopId, state);
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody Staff staff){
        return staffService.add(staff);
    }
    @PutMapping("/update")
    public Result<?> updateStaff(@RequestBody Staff staff){
        return staffService.updateStaff(staff);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return staffService.delete(id);
    }
    @GetMapping("/simpleListByShopId")
    public Result<List<Staff>> simpleListByShopId(@RequestParam String shopId){
        return staffService.simpleListByShopId(shopId);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String account,@RequestParam String password,@RequestParam Integer perssion){
        return staffService.login(account,password,perssion);
    }

}
