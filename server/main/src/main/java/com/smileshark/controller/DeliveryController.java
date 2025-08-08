package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.Delivery;
import com.smileshark.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 配送的配置有点多，就分开了 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;
    @GetMapping("/infoById")
    public Result<Delivery> infoById(String id) {
        return deliveryService.infoById(id);
    }
    @PostMapping("/saveOrUpdate")
    public Result<?> saveOrUpdate(@RequestBody Delivery delivery) {
        return deliveryService.saveOrUpdateDelivery(delivery);
    }
}
