package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.DiningTableType;
import com.smileshark.service.DiningTableTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 餐桌类型 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/diningTableType")
@RequiredArgsConstructor
public class DiningTableTypeController {
    private final DiningTableTypeService diningTableTypeService;

    @GetMapping("/list")
    public Result<List<DiningTableType>> list() {
        return diningTableTypeService.diningTableTypeList();
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody DiningTableType diningTableType){
        return diningTableTypeService.add(diningTableType);
    }
    @PutMapping("/update")
    public Result<?> update(@RequestBody DiningTableType diningTableType){{
        return diningTableTypeService.updateDiningTableType(diningTableType);}
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return diningTableTypeService.delete(id);
    }
}
