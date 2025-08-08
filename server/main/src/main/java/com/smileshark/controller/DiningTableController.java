package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.DiningTable;
import com.smileshark.service.DiningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 餐桌 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/diningTable")
@RequiredArgsConstructor
public class DiningTableController {
    private final DiningTableService diningTableService;

    @GetMapping("/pageList")
    public Result<Page<DiningTable>> pageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,@RequestParam(required = false) String shopId, @RequestParam(required = false) String diningTableTypeId, @RequestParam(required = false)String userState){
        return diningTableService.pageList(page, size, shopId, diningTableTypeId, userState);
    }
    @GetMapping("/listByShopId")
    public Result<List<DiningTable>> list(String shopId){
        return diningTableService.listByShopId(shopId);
    }
    @PostMapping("/add")
    public Result<?> add(@RequestBody DiningTable diningTable){
        return diningTableService.add(diningTable);
    }
    @PutMapping("/update")
    public Result<?> update(@RequestBody DiningTable diningTable){
        return diningTableService.updateDiningTable(diningTable);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return diningTableService.delete(id);
    }
}
