package com.smileshark.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Wallet;
import com.smileshark.mapper.WalletMapper;
import com.smileshark.service.WalletService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 钱包 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImp extends ServiceImpl<WalletMapper, Wallet> implements WalletService {

    @Override
    public Result<Wallet> infoById(String walletId) {
        return Result.success(baseMapper.selectById(walletId));
    }

    @Override
    public Result<?> recharge(String walletId, Double amount) {
        update(new LambdaUpdateWrapper<>(Wallet.class).setSql("balance = balance + " + amount).eq(Wallet::getWalletId, walletId));
        return Result.success(ResultCode.RECHARGE_SUCCESS);
    }
}
