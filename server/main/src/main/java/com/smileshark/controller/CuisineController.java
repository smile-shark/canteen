package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Cuisine;
import com.smileshark.service.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品详情 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/cuisine")
@RequiredArgsConstructor
public class CuisineController {
    private final CuisineService cuisineService;

    @GetMapping("/pageList")
    public Result<Page<Cuisine>> pageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String cuisineName, @RequestParam(required = false) String cuisineType, @RequestParam(required = false) Integer state) {
        return cuisineService.pageList(page, size, cuisineName, cuisineType, state);
    }
    @GetMapping("/inventoryPageList")
    public Result<Page<Cuisine>> inventoryPageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String name, @RequestParam(required = false) String cuisineTypeId, @RequestParam(required = false) String shopId){
        return cuisineService.inventoryPageList(page, size, name, cuisineTypeId, shopId);
    }
    @GetMapping("/infoById")
    public Result<Cuisine> infoById(@RequestParam String id){
        return cuisineService.getInfoById(id);
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody Cuisine cuisine){
        return cuisineService.add(cuisine);
    }
    @PutMapping("/update")
    public Result<?> update(@RequestBody Cuisine cuisine){
        return cuisineService.updateCuisine(cuisine);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        return cuisineService.deleteCuisine(id);
    }
    // 用户点餐时调用的菜品列表
    @GetMapping("/cuisineServiceList")
    public Result<List<Cuisine>> cuisineServiceList(@RequestParam String name, @RequestParam Integer serviceType, @RequestParam String cuisineTypeId,@RequestParam String shopId){
        return cuisineService.cuisineServiceList(name, serviceType, cuisineTypeId,shopId);
    }
    // 根据id获取详细的信息
    @GetMapping("/getSimpleCuisineById")
    public Result<Cuisine> getSimpleCuisineById(@RequestParam String id){
        return cuisineService.getCuisineById(id);
    }

}
