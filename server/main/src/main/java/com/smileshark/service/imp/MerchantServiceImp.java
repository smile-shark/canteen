package com.smileshark.service.imp;

import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Merchant;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.MerchantMapper;
import com.smileshark.service.MerchantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商户 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class MerchantServiceImp extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {
    private final TokenUtil tokenUtil;
    @Override
    public Result<String> login(String account, String password) {
        Merchant merchant = lambdaQuery().eq(Merchant::getAccount, account).one();
        if(merchant==null){
            throw new BusinessException(ResultCode.NOT_HAVE_ACCOUNT);
        }
        if(!merchant.getPassword().equals(password)){
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        merchant.setPassword(null);
        merchant.setAccount(null);
        return Result.success(tokenUtil.createToken(merchant));
    }
}
