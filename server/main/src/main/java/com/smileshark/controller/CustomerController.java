package com.smileshark.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Customer;
import com.smileshark.service.CustomerService;
import com.smileshark.service.imp.CustomerServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 客户 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/passwordLogin")
    public Result<Map<String,Object>> loginForPassword(@RequestParam String account, @RequestParam String password){
        return customerService.loginForPassword(account, password);
    }

    @PostMapping("/verifyCodeLogin")
    public Result<Map<String,Object>>  loginForVerifyCode(@RequestParam String account,@RequestParam String verifyCode){
        return customerService.loginForVerifyCode(account, verifyCode);
    }

        @PostMapping("/register")
    public Result<Map<String,Object>> register(@RequestBody Customer customer,@RequestParam String verifyCode){
        return customerService.register(customer, verifyCode);
    }
    @GetMapping("/pageList")
    public Result<Page<Customer>> pageList(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "10")Integer size,@RequestParam(required = false) String name,@RequestParam(required = false) String shopId){
        return  customerService.pageList(page,size,name,shopId);
    }
}
