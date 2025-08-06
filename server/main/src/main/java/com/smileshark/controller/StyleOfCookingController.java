package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.StyleOfCooking;
import com.smileshark.service.StyleOfCookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜系 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/styleOfCooking")
@RequiredArgsConstructor
public class StyleOfCookingController {
    private final StyleOfCookingService styleOfCookingService;
    @GetMapping("/list")
    public Result<List<StyleOfCooking>> list(){
        return styleOfCookingService.styleOfCookingList();
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody StyleOfCooking styleOfCooking){
        return styleOfCookingService.add(styleOfCooking);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return styleOfCookingService.delete(id);
    }
}
