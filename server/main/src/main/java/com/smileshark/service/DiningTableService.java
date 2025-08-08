package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.DiningTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 餐桌 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface DiningTableService extends IService<DiningTable> {

    Result<Page<DiningTable>> pageList(Integer page, Integer size, String shopId, String diningTableTypeId, String userState);

    Result<?> add(DiningTable diningTable);

    Result<?> updateDiningTable(DiningTable diningTable);

    Result<?> delete(String id);

    Result<List<DiningTable>> listByShopId(String shopId);
}
