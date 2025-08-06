package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.RawMaterialType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 原料分类表 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface RawMaterialTypeService extends IService<RawMaterialType> {

    Result<List<RawMaterialType>> rawMaterialTypeList();

    Result<?> add(RawMaterialType rawMaterialType);

    Result<?> delete(String id);
}
