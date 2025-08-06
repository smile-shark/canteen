package com.smileshark.mapper;

import com.smileshark.entity.Delivery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 配送的配置有点多，就分开了 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月04日
 */
@Mapper
public interface DeliveryMapper extends BaseMapper<Delivery> {

}
