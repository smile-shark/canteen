package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 区域 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface AreaService extends IService<Area> {

    Result<List<Area>> listById(String id);

    Result<Area> infoByProvinceId(String AreaId);
}
