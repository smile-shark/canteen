package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.RawMaterial;
import com.smileshark.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 原料表 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/rawMaterial")
@RequiredArgsConstructor
public class RawMaterialController {
    private final RawMaterialService rawMaterialService;
    @GetMapping("/pageList")
    public Result<Page<RawMaterial>> pageList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String rawMaterialTypeId,
            @RequestParam(required = false) String shopId
    ){
        return rawMaterialService.pageList(page, size, name, rawMaterialTypeId,shopId);
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody RawMaterial rawMaterial){
        return rawMaterialService.add(rawMaterial);
    }
    @PutMapping("/update")
    public Result<?> update(@RequestBody RawMaterial rawMaterial){
        return rawMaterialService.updateRawMaterial(rawMaterial);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return rawMaterialService.delete(id);
    }
}
