package com.smileshark.service.imp;

import com.smileshark.common.Result;
import com.smileshark.entity.Delivery;
import com.smileshark.mapper.DeliveryMapper;
import com.smileshark.service.DeliveryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配送的配置有点多，就分开了 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class DeliveryServiceImp extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {

    @Override
    public Result<Delivery> infoById(String id) {
        return Result.success(lambdaQuery().eq(Delivery::getShopId, id).one());
    }

    @Override
    public Result<?> saveOrUpdateDelivery(Delivery delivery) {
        return Result.success(saveOrUpdate(delivery));
    }
}
