package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.CuisineType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品的分类 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface CuisineTypeService extends IService<CuisineType> {

    Result<List<CuisineType>> cuisineTypeList();

    Result<?> add(CuisineType cuisineType);

    Result<?> cuisineTypeDelete(String id);
}
