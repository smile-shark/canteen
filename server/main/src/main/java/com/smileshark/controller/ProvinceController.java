package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.Province;
import com.smileshark.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 省 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/province")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;
    @GetMapping("/list")
    public Result<List<Province>> list(){
        return Result.success(provinceService.list());
    }
}
