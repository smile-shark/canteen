package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.CustomerOrderCuisine;
import com.smileshark.service.CustomerOrderCuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 订单菜品列表 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/customerOrderCuisine")
@RequiredArgsConstructor
public class CustomerOrderCuisineController {
    private final CustomerOrderCuisineService customerOrderCuisineService;
    @GetMapping("/listByCustomerOrderId")
    public Result<List<CustomerOrderCuisine>> listByCustomerOrderId(@RequestParam String customerOrderId){
        return customerOrderCuisineService.listByCustomerOrderId(customerOrderId);
    }

}
