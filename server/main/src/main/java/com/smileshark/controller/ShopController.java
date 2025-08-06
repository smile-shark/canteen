package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Shop;
import com.smileshark.exception.BusinessException;
import com.smileshark.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 门店表 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
private final ShopService shopService;
    @PostMapping("/add")
    public Result<?> add(@RequestBody Shop shop){
        return shopService.addShop(shop);
    }
    @GetMapping("/pageList")
    public Result<Page<Shop>> pageList(@RequestParam(defaultValue = "1") Integer page,@RequestParam (defaultValue = "10") Integer size,@RequestParam(required = false) String num,@RequestParam(required = false) Integer state){
        return shopService.pageList(page, size,num,state);
    }
    @PutMapping("/update")
    public Result<?> update(@RequestBody Shop shop){
        return shopService.updateById(shop) ? Result.success(ResultCode.UPDATE_SUCCESS) : Result.error(ResultCode.UPDATE_ERROR);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return shopService.removeById(id) ? Result.success(ResultCode.DELETE_SUCCESS) : Result.error(ResultCode.DELETE_ERROR);
    }
    @GetMapping("/simpleList")
    public Result<List<Shop>> simpleList(){
        return shopService.simpleList();
    }
}
