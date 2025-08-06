package com.smileshark.service;

import com.smileshark.common.Result;
import com.smileshark.entity.Taste;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 口味 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface TasteService extends IService<Taste> {

    Result<List<Taste>> tasteList();

    Result<?> add(Taste taste);

    Result<?> delete(String id);
}
