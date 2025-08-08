package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.Delivery;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 配送的配置有点多，就分开了 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface DeliveryService extends IService<Delivery> {


    Result<?> saveOrUpdateDelivery(Delivery delivery);

    Result<Delivery> infoById(String id);
}
