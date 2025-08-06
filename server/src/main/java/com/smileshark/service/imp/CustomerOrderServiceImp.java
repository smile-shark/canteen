package com.smileshark.service.imp;

import com.smileshark.entity.CustomerOrder;
import com.smileshark.mapper.CustomerOrderMapper;
import com.smileshark.service.CustomerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户订单 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Service
public class CustomerOrderServiceImp extends ServiceImpl<CustomerOrderMapper, CustomerOrder> implements CustomerOrderService {

}
