package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.CuisineComponent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品的分量/规格 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface CuisineComponentService extends IService<CuisineComponent> {

    Result<String> addCuisineComponent(CuisineComponent cuisineComponent);

    Result<CuisineComponent> getCuisineComponentById(String id);

    Result<String> updateCuisineComponent(CuisineComponent cuisineComponent);

    Result<String> deleteCuisineComponent(String id);

    Result<List<CuisineComponent>> getCuisineComponentList();
}
