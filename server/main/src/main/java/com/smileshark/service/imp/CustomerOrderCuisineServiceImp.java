package com.smileshark.service.imp;

import com.smileshark.common.Result;
import com.smileshark.entity.Cuisine;
import com.smileshark.entity.CustomerOrderCuisine;
import com.smileshark.mapper.CuisineMapper;
import com.smileshark.mapper.CustomerOrderCuisineMapper;
import com.smileshark.service.CustomerOrderCuisineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单菜品列表 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class CustomerOrderCuisineServiceImp extends ServiceImpl<CustomerOrderCuisineMapper, CustomerOrderCuisine> implements CustomerOrderCuisineService {

    private final CuisineMapper cuisineMapper;

    @Override
    public Result<List<CustomerOrderCuisine>> listByCustomerOrderId(String customerOrderId) {
        List<CustomerOrderCuisine> list = lambdaQuery().eq(CustomerOrderCuisine::getCustomerOrderId, customerOrderId).list();
        for (CustomerOrderCuisine customerOrderCuisine : list) {
            Cuisine cuisine = cuisineMapper.selectById(customerOrderCuisine.getCuisineId());
            customerOrderCuisine.setName(cuisine.getName());
            customerOrderCuisine.setCuisineNum(cuisine.getNum());
            customerOrderCuisine.setPrice(cuisine.getPrice());
        }
        return Result.success(list);
    }
}
