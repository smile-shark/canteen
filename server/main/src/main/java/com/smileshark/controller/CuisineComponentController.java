package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.CuisineComponent;
import com.smileshark.service.CuisineComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品的分量/规格 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@RestController
@RequestMapping("/cuisineComponent")
@RequiredArgsConstructor
public class CuisineComponentController {
    private final CuisineComponentService cuisineComponentService;
    @GetMapping("/list")
    public Result<List<CuisineComponent>> getCuisineComponentList() {
        return cuisineComponentService.getCuisineComponentList();
    }
    @PostMapping("/add")
    public Result<?> addCuisineComponent(@RequestBody CuisineComponent cuisineComponent){
        return  cuisineComponentService.addCuisineComponent(cuisineComponent);
    }
    @DeleteMapping("/delete")
    public Result<String> deleteCuisineComponent(@RequestParam String id){
        return cuisineComponentService.deleteCuisineComponent(id);
    }

}
