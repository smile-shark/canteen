package com.smileshark.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.entity.StaffLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * <p>
 * 员工登录日志 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Mapper
public interface StaffLoginLogMapper extends BaseMapper<StaffLoginLog> {

    Page<StaffLoginLog> pageList(Page<StaffLoginLog> page, @Param("shopId") String shopId,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
}
