package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.Area;
import com.smileshark.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 区域 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/area")
@RequiredArgsConstructor
public class AreaController {
private final AreaService areaService;
    @GetMapping("/listById")
    public Result<List<Area>> listById(String id){
        return areaService.listById(id);
    }
}
