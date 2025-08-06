package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.StyleOfCooking;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜系 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface StyleOfCookingService extends IService<StyleOfCooking> {

    Result<List<StyleOfCooking>> styleOfCookingList();

    Result<?> add(StyleOfCooking styleOfCooking);

    Result<?> delete(String id);
}
