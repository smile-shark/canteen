package com.smileshark.service.imp;

import com.smileshark.entity.CuisineImage;
import com.smileshark.mapper.CuisineImageMapper;
import com.smileshark.service.CuisineImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品的图片，一个菜可能有多个图片 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
public class CuisineImageServiceImp extends ServiceImpl<CuisineImageMapper, CuisineImage> implements CuisineImageService {

}
