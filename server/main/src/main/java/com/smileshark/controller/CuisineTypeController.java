package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.CuisineType;
import com.smileshark.service.CuisineTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品的分类 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/cuisineType")
@RequiredArgsConstructor
public class CuisineTypeController {
    private final CuisineTypeService cuisineTypeService;
    @GetMapping("/list")
    public Result<List<CuisineType>> list() {
        return cuisineTypeService.cuisineTypeList();
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody CuisineType cuisineType) {
        return cuisineTypeService.add(cuisineType);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        return cuisineTypeService.cuisineTypeDelete(id);
    }
}
