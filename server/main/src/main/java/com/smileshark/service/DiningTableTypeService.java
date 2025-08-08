package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.DiningTableType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 餐桌类型 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface DiningTableTypeService extends IService<DiningTableType> {

    Result<List<DiningTableType>> diningTableTypeList();

    Result<?> add(DiningTableType diningTableType);

    Result<?> updateDiningTableType(DiningTableType diningTableType);

    Result<?> delete(String id);
}
