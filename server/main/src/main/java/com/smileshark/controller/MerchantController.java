package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商户 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;
    @RequestMapping("/login")
    public Result<String> login(@RequestParam String account,@RequestParam String password){
        return merchantService.login(account,password);
    }
}
