package com.smileshark.mapper;

import com.smileshark.entity.CuisineImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品的图片，一个菜可能有多个图片 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Mapper
public interface CuisineImageMapper extends BaseMapper<CuisineImage> {

}
