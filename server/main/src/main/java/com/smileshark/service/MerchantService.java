package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.Merchant;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商户 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface MerchantService extends IService<Merchant> {

    Result<String> login(String account, String password);
}
