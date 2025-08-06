package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.smileshark.common.Result;
import com.smileshark.entity.CuisineComponent;
import com.smileshark.mapper.CuisineComponentMapper;
import com.smileshark.service.CuisineComponentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品的分量/规格 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class CuisineComponentServiceImp extends ServiceImpl<CuisineComponentMapper, CuisineComponent> implements CuisineComponentService {

    private final CuisineComponentMapper cuisineComponentMapper;

    @Override
    public Result<String> addCuisineComponent(CuisineComponent cuisineComponent) {
        try {
            cuisineComponent.setCuisineComponentId(IdUtil.simpleUUID());
            boolean saved = this.save(cuisineComponent);
            if (saved) {
                return Result.success("添加成功");
            } else {
                return Result.error("添加失败");
            }
        } catch (Exception e) {
            return Result.error("添加失败: " + e.getMessage());
        }
    }

    @Override
    public Result<CuisineComponent> getCuisineComponentById(String id) {
        try {
            CuisineComponent cuisineComponent = this.getById(id);
            if (cuisineComponent != null) {
                return Result.success(cuisineComponent);
            } else {
                return Result.error("未找到指定的菜品分量/规格");
            }
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @Override
    public Result<String> updateCuisineComponent(CuisineComponent cuisineComponent) {
        try {
            boolean updated = this.updateById(cuisineComponent);
            if (updated) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    @Override
    public Result<String> deleteCuisineComponent(String id) {
        try {
            boolean removed = this.removeById(id);
            if (removed) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CuisineComponent>> getCuisineComponentList() {
        return Result.success(lambdaQuery().last("order by num asc").list());
    }
}
