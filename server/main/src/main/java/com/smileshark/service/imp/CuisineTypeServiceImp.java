package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.smileshark.common.Result;
import com.smileshark.entity.CuisineType;
import com.smileshark.mapper.CuisineTypeMapper;
import com.smileshark.service.CuisineTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品的分类 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class CuisineTypeServiceImp extends ServiceImpl<CuisineTypeMapper, CuisineType> implements CuisineTypeService {

    @Override
    public Result<List<CuisineType>> cuisineTypeList() {
        return Result.success(lambdaQuery().last("order by num asc").list());
    }

    @Override
    public Result<?> add(CuisineType cuisineType) {
        cuisineType.setCuisineTypeId(IdUtil.simpleUUID());
        return Result.success(save(cuisineType));
    }

    @Override
    public Result<?> cuisineTypeDelete(String id) {
        return Result.success(removeById(id));
    }
}
