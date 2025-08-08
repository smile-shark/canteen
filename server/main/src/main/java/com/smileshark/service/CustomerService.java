package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface CustomerService extends IService<Customer> {

    Result<Map<String, Object>> loginForPassword(String account, String password);

    Result<Map<String, Object>> loginForVerifyCode(String account, String verifyCode);

    Result<Map<String, Object>> register(Customer customer, String verifyCode);
}
