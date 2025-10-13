package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.CustomerOrderCuisine;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单菜品列表 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface CustomerOrderCuisineService extends IService<CustomerOrderCuisine> {

    Result<List<CustomerOrderCuisine>> listByCustomerOrderId(String customerOrderId);
}
