package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.Wallet;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 钱包 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface WalletService extends IService<Wallet> {

    Result<Wallet> infoById(String walletId);
}
