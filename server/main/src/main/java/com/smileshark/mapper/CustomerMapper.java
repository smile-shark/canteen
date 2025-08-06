package com.smileshark.mapper;

import com.smileshark.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
