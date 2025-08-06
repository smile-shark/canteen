package com.smileshark.service.imp;

import com.smileshark.entity.Customer;
import com.smileshark.mapper.CustomerMapper;
import com.smileshark.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Service
public class CustomerServiceImp extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
