package com.smileshark.service.imp;

import cn.hutool.core.bean.BeanUtil;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Customer;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.CustomerMapper;
import com.smileshark.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}