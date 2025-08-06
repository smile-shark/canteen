package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Cuisine;
import com.smileshark.entity.CuisineImage;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.CuisineImageMapper;
import com.smileshark.mapper.CuisineMapper;
import com.smileshark.service.CuisineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 菜品详情 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class CuisineServiceImp extends ServiceImpl<CuisineMapper, Cuisine> implements CuisineService {
private final CuisineMapper cuisineMapper;
private final CuisineImageMapper cuisineImageMapper;
    @Override
    public Result<Page<Cuisine>> pageList(Integer page, Integer size, String cuisineName, String cuisineType, Integer state) {
        return Result.success(cuisineMapper.pageList(new Page<>(page,size), StrUtil.globbing(cuisineName), cuisineType, state));
    }

    @Override
    @Transactional
    public Result<?> add(Cuisine cuisine) {
        String cuisineId = IdUtil.simpleUUID();
        cuisine.setCuisineId(cuisineId);
        // 添加菜品
        if (!saveOrUpdate(cuisine)) {
            throw new BusinessException(ResultCode.ADD_ERROR);
        }
        // 添加菜品图片
        for (CuisineImage cuisineImage : cuisine.getCuisineImages()) {
            cuisineImage.setCuisineId(cuisineId);
            if (cuisineImageMapper.insert(cuisineImage)<=0) {
                throw new BusinessException(ResultCode.ADD_ERROR);
            }
        }
        return Result.success(ResultCode.ADD_SUCCESS);
    }

    @Override
    @Transactional
    public Result<?> deleteCuisine(String id) {
        // 删除菜品图片
        cuisineImageMapper.delete(new LambdaQueryWrapper<>(CuisineImage.class).eq(CuisineImage::getCuisineId, id));
        // 删除菜品
        if (!removeById(id)) {
            throw new BusinessException(ResultCode.DELETE_ERROR);
        }
        return Result.success(ResultCode.DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public Result<?> updateCuisine(Cuisine cuisine) {
        // 删除菜品图片
        cuisineImageMapper.delete(new LambdaQueryWrapper<>(CuisineImage.class).eq(CuisineImage::getCuisineId, cuisine.getCuisineId()));
        // 添加新的图片
        for (CuisineImage cuisineImage : cuisine.getCuisineImages()) {
            cuisineImage.setCuisineId(cuisine.getCuisineId());
            if (cuisineImageMapper.insert(cuisineImage)<=0) {
                throw new BusinessException(ResultCode.UPDATE_ERROR);
            }
        }
        // 修改菜品
        if (!updateById(cuisine)) {
            throw new BusinessException(ResultCode.UPDATE_ERROR);
        }
        return Result.success(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    public Result<Cuisine> getInfoById(String id) {
        Cuisine cuisine = lambdaQuery().eq(Cuisine::getCuisineId, id).one();
        cuisine.setCuisineImages(cuisineImageMapper.selectList(new LambdaQueryWrapper<>(CuisineImage.class).eq(CuisineImage::getCuisineId, id).last("order by image_order asc")));
        return Result.success(cuisine);
    }
}
