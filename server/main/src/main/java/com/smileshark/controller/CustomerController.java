package com.smileshark.controller;

import com.smileshark.common.Result;
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

    @PostMapping("/password/login")
    public Result<Map<String,Object>> loginForPassword(@RequestParam String account, @RequestParam String password){
        return customerService.loginForPassword(account, password);
    }
}
