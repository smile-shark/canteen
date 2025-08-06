package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.smileshark.common.Result;
import com.smileshark.entity.RawMaterialType;
import com.smileshark.mapper.RawMaterialTypeMapper;
import com.smileshark.service.RawMaterialTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 原料分类表 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class RawMaterialTypeServiceImp extends ServiceImpl<RawMaterialTypeMapper, RawMaterialType> implements RawMaterialTypeService {

    @Override
    public Result<List<RawMaterialType>> rawMaterialTypeList() {
        return Result.success(lambdaQuery().last("order by num asc").list());
    }

    @Override
    public Result<?> add(RawMaterialType rawMaterialType) {
        rawMaterialType.setRawMaterialTypeId(IdUtil.simpleUUID());
        return Result.success(save(rawMaterialType));
    }

    @Override
    public Result<?> delete(String id) {
        return Result.success(removeById(id));
    }
}
