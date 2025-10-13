package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.Wallet;
import com.smileshark.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 钱包 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    @GetMapping("/infoById")
    public Result<Wallet> infoById(String id) {
        return walletService.infoById(id);
    }
    @PostMapping("/recharge")
    public Result<?> recharge(@RequestParam String walletId, @RequestParam Double amount) {
        return walletService.recharge(walletId, amount);
    }
}
