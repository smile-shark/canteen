package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 门店表 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface ShopService extends IService<Shop> {

    Result<?> addShop(Shop shop);

    Result<Page<Shop>> pageList(Integer page, Integer size, String num, Integer state);

    Result<List<Shop>> simpleList();

    Result<Page<Shop>> pageListDineIn(Integer page, Integer size, String shopId, Integer isDineIn);

    Result<Page<Shop>> pageListTakeOut(Integer page, Integer size, String shopId, Integer isTakeOut);

    Result<List<Shop>> pageListByRange(Integer page, Integer size, Integer isDineIn, Integer isTakeOut);

    Result<Shop> infoById(String id);

    Result<?> delete(String id);
}
