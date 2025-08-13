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
        return shopService.updateShop(shop);
    }
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id){
        return shopService.delete(id);
    }
    @GetMapping("/simpleList")
    public Result<List<Shop>> simpleList(){
        return shopService.simpleList();
    }
    @GetMapping("/pageListDineIn")
    public Result<Page<Shop>> pageListDineIn(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String shopId, @RequestParam(required = false) Integer isDineIn){
        return shopService.pageListDineIn(page, size, shopId, isDineIn);
    }
    @GetMapping("/pageListTakeOut")
    public Result<Page<Shop>> pageListTakeOut(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String shopId, @RequestParam(required = false) Integer isTakeOut){
        return shopService.pageListTakeOut(page, size, shopId, isTakeOut);
    }
    @GetMapping("/pageListByRange")
    public Result<List<Shop>> pageListByRange(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Integer isDineIn,@RequestParam(required = false)Integer isTakeOut ,@RequestParam(required = false) Double longitude,@RequestParam(required = false) Double latitude){
        return shopService.pageListByRange(page, size, isDineIn, isTakeOut, longitude, latitude);
    }
    @GetMapping("/infoById")
    public Result<Shop> infoById(@RequestParam String id){
        return shopService.infoById(id);
    }
}
