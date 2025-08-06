package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.smileshark.common.Result;
import com.smileshark.entity.StyleOfCooking;
import com.smileshark.mapper.StyleOfCookingMapper;
import com.smileshark.service.StyleOfCookingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜系 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class StyleOfCookingServiceImp extends ServiceImpl<StyleOfCookingMapper, StyleOfCooking> implements StyleOfCookingService {

    @Override
    public Result<List<StyleOfCooking>> styleOfCookingList() {
        return Result.success(lambdaQuery().last("order by num asc").list());
    }

    @Override
    public Result<?> add(StyleOfCooking styleOfCooking) {
        styleOfCooking.setStyleOfCookingId(IdUtil.simpleUUID());
        return Result.success(save(styleOfCooking));
    }

    @Override
    public Result<?> delete(String id) {
        return Result.success(removeById(id));
    }
}
