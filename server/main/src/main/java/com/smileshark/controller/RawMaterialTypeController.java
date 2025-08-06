package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.RawMaterialType;
import com.smileshark.service.RawMaterialTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 原料分类表 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/rawMaterialType")
@RequiredArgsConstructor
public class RawMaterialTypeController {
    private final RawMaterialTypeService rawMaterialTypeService;

    @GetMapping("/list")
    public Result<List<RawMaterialType>> list() {
        return rawMaterialTypeService.rawMaterialTypeList();
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody RawMaterialType rawMaterialType){
        return rawMaterialTypeService.add(rawMaterialType);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return rawMaterialTypeService.delete(id);
    }
}
