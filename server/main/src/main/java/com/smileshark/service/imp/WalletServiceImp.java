package com.smileshark.service.imp;

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
}
