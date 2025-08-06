package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.smileshark.common.Result;
import com.smileshark.entity.Taste;
import com.smileshark.mapper.TasteMapper;
import com.smileshark.service.TasteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 口味 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class TasteServiceImp extends ServiceImpl<TasteMapper, Taste> implements TasteService {

    @Override
    public Result<List<Taste>> tasteList() {
        return Result.success(lambdaQuery().last("order by num asc").list());
    }

    @Override
    public Result<?> add(Taste taste) {
        taste.setTasteId(IdUtil.simpleUUID());
        return Result.success(save(taste));
    }

    @Override
    public Result<?> delete(String id) {
        return Result.success(removeById(id));
    }
}
