package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Cuisine;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品详情 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface CuisineService extends IService<Cuisine> {

    Result<Page<Cuisine>> pageList(Integer page, Integer size, String cuisineName, String cuisineType, Integer state);

    Result<?> add(Cuisine cuisine);

    Result<?> deleteCuisine(String id);

    Result<?> updateCuisine(Cuisine cuisine);

    Result<Cuisine> getInfoById(String id);

    Result<Page<Cuisine>> inventoryPageList(Integer page, Integer size, String name, String cuisineTypeId, String shopId);

    Result<List<Cuisine>> cuisineServiceList(String name, Integer serviceType, String cuisineTypeId, String shopId);

    Result<Cuisine> getCuisineById(String id);
}
