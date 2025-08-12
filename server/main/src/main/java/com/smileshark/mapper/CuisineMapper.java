package com.smileshark.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.entity.Cuisine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜品详情 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Mapper
public interface CuisineMapper extends BaseMapper<Cuisine> {

    Page<Cuisine> pageList(Page<Cuisine> objectPage, String name, String cuisineType, Integer state);

    List<Cuisine> cuisineServiceList(@Param("name") String name, @Param("serviceType") Integer serviceType, @Param("cuisineTypeId") String cuisineTypeId, @Param("shopId") String shopId);

    Cuisine selectCuisineById(@Param("id") String id);
}
