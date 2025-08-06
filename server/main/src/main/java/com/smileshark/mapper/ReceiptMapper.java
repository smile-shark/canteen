package com.smileshark.mapper;

import com.smileshark.entity.Receipt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 小票的样式管理 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {

}
