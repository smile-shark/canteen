package com.smileshark.service.imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Customer;
import com.smileshark.entity.Wallet;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.CustomerMapper;
import com.smileshark.mapper.WalletMapper;
import com.smileshark.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.service.WalletService;
import com.smileshark.service.global.EmailService;
import com.smileshark.utils.StrUtil;
import com.smileshark.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImp extends ServiceImpl<CustomerMapper, Customer> implements  CustomerService {

    private final CustomerMapper customerMapper;
    private final TokenUtil tokenUtil;
    private final EmailService emailService;
    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Override
    public Result<Map<String, Object>> loginForPassword(String account, String password) {
        // 1. 根据账号查询客户
        Customer customer = lambdaQuery()
                .eq(Customer::getAccount, account)
                .or()
                .eq(Customer::getPhone, account)
                .one();

        // 2. 验证账号是否存在
        if (customer == null) {
            throw new BusinessException(ResultCode.NOT_HAVE_ACCOUNT);
        }

        // 3. 验证密码是否正确
        if (!password.equals(customer.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 4. 登录成功，返回用户信息
        customer.setPassword(null);
        Map<String, Object> result = BeanUtil.beanToMap(customer);
        result.put("token", tokenUtil.createToken(customer));

        return Result.success("登录成功", result);
    }

    @Override
    public Result<Map<String, Object>> loginForVerifyCode(String account, String verifyCode) {
        if(!emailService.verifyCode(account,verifyCode)){
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }
        // 查询是否有该账户
        Customer customer = lambdaQuery().eq(Customer::getAccount, account).one();
        if (customer == null) {
            throw new BusinessException(ResultCode.NOT_HAVE_ACCOUNT);
        }

        customer.setPassword(null);
        Map<String, Object> result = BeanUtil.beanToMap(customer);
        result.put("token", tokenUtil.createToken(customer));

        return Result.success("登录成功", result);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> register(Customer customer, String verifyCode) {
        if(!emailService.verifyCode(customer.getAccount(),verifyCode)){
            throw new BusinessException(ResultCode.VERIFY_CODE_ERROR);
        }

        // 为用户添加钱包
        String walletId = IdUtil.simpleUUID();
        walletMapper.insert(new Wallet(walletId,0d,null));

        // 注册账户
        customer.setCustomerId(IdUtil.simpleUUID());
        customer.setWalletId(walletId);
        customer.setIntegral(0);
        customer.setLevel(0);
        customer.setSex(0);
        customerMapper.insert(customer);

        customer.setPassword(null);
        Map<String, Object> result = BeanUtil.beanToMap(customer);
        result.put("token", tokenUtil.createToken(customer));

        return Result.success("注册成功", result);
    }

    @Override
    public Result<Page<Customer>> pageList(Integer page, Integer size, String name, String shopId) {
        Page<Customer> customerPage=customerMapper.pageList(new Page<>(page, size), StrUtil.globbing(name),shopId);

        return Result.success(customerPage);
    }
}