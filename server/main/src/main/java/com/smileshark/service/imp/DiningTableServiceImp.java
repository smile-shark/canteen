package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.DiningTable;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.DiningTableMapper;
import com.smileshark.service.DiningTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 餐桌 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class DiningTableServiceImp extends ServiceImpl<DiningTableMapper, DiningTable> implements DiningTableService {

    @Override
    public Result<Page<DiningTable>> pageList(Integer page, Integer size, String shopId, String diningTableTypeId, String userState) {
        System.out.println("page = " + page);
        System.out.println("size = " + size);
        LambdaQueryChainWrapper<DiningTable> query = lambdaQuery();
        if(shopId != null && !shopId.isEmpty()){
            query.eq(DiningTable::getShopId, shopId);
        }
        if(diningTableTypeId != null && !diningTableTypeId.isEmpty()){
            query.eq(DiningTable::getDiningTableTypeId, diningTableTypeId);
        }
        if(userState != null && !userState.isEmpty()){
            query.eq(DiningTable::getUserState, userState);
        }
        return Result.success(query.last("order by serial_number").page(new Page<>(page, size)));
    }

    @Override
    public Result<?> add(DiningTable diningTable) {
        diningTable.setDiningTableId(IdUtil.simpleUUID());
        if (!save(diningTable)) {
            throw new BusinessException(ResultCode.ADD_ERROR);
        }
        return Result.success(diningTable);
    }

    @Override
    public Result<?> updateDiningTable(DiningTable diningTable) {
        return Result.success(updateById(diningTable));
    }

    @Override
    public Result<?> delete(String id) {
        return Result.success(removeById(id));
    }

    @Override
    public Result<List<DiningTable>> listByShopId(String shopId) {
        return Result.success(lambdaQuery().eq(DiningTable::getShopId, shopId).list());
    }
}
