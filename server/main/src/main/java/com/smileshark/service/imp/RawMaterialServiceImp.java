package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.RawMaterial;
import com.smileshark.mapper.RawMaterialMapper;
import com.smileshark.service.RawMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 原料表 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class RawMaterialServiceImp extends ServiceImpl<RawMaterialMapper, RawMaterial> implements RawMaterialService {

    @Override
    public Result<Page<RawMaterial>> pageList(Integer page, Integer size, String name, String rawMaterialTypeId, String shopId) {
        LambdaQueryChainWrapper<RawMaterial> query = lambdaQuery().like(RawMaterial::getName, StrUtil.globbing(name));
        if(rawMaterialTypeId!=null && !rawMaterialTypeId.isEmpty()){
            query.eq(RawMaterial::getRawMaterialTypeId, rawMaterialTypeId);
        }
        if(shopId!=null && !shopId.isEmpty()){
            query.eq(RawMaterial::getShopId, shopId);
        }
        return Result.success(query.page(new Page<>(page, size)));
    }

    @Override
    public Result<?> add(RawMaterial rawMaterial) {
        rawMaterial.setRawMaterialId(IdUtil.simpleUUID());
        return Result.success(save(rawMaterial));
    }

    @Override
    public Result<?> updateRawMaterial(RawMaterial rawMaterial) {
        return Result.success(updateById(rawMaterial));
    }

    @Override
    public Result<?> delete(String id) {
        return Result.success(removeById(id));
    }
}
