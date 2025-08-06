package com.smileshark.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.entity.Staff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 员工 Mapper 接口
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Mapper
public interface StaffMapper extends BaseMapper<Staff> {

    Page<Staff> selectStaffPageList(Page<Staff> staffPage, @Param("num") String num, @Param("shopId") String shopId, @Param("state") Integer state);
}
