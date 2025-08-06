package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.RawMaterial;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 原料表 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface RawMaterialService extends IService<RawMaterial> {

    Result<Page<RawMaterial>> pageList(Integer page, Integer size, String name, String rawMaterialTypeId);

    Result<?> add(RawMaterial rawMaterial);

    Result<?> updateRawMaterial(RawMaterial rawMaterial);

    Result<?> delete(String id);
}
