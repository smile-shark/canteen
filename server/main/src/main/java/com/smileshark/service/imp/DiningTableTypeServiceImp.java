package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.smileshark.common.Result;
import com.smileshark.entity.DiningTableType;
import com.smileshark.mapper.DiningTableTypeMapper;
import com.smileshark.service.DiningTableTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 餐桌类型 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class DiningTableTypeServiceImp extends ServiceImpl<DiningTableTypeMapper, DiningTableType> implements DiningTableTypeService {

    @Override
    public Result<List<DiningTableType>> diningTableTypeList() {
        return Result.success(lambdaQuery().last("order by type,min").list());
    }

    @Override
    public Result<?> add(DiningTableType diningTableType) {
        diningTableType.setDiningTableTypeId(IdUtil.simpleUUID());
        return Result.success(save(diningTableType));
    }

    @Override
    public Result<?> updateDiningTableType(DiningTableType diningTableType) {
        if(diningTableType.getDiningTableTypeId() == null|| diningTableType.getDiningTableTypeId().isEmpty()
        )diningTableType.setDiningTableTypeId(IdUtil.simpleUUID());
        return Result.success(saveOrUpdate(diningTableType));
    }

    @Override
    public Result<?> delete(String id) {
        return Result.success(removeById(id));
    }
}
