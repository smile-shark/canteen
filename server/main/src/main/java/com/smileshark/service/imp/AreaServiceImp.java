package com.smileshark.service.imp;

import com.smileshark.common.Result;
import com.smileshark.entity.Area;
import com.smileshark.mapper.AreaMapper;
import com.smileshark.service.AreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 区域 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class AreaServiceImp extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Override
    public Result<List<Area>> listById(String id) {
        return Result.success(lambdaQuery().eq(Area::getProvinceId,id).list());
    }
}
