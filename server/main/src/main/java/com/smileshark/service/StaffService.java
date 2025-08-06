package com.smileshark.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.common.Result;
import com.smileshark.entity.Staff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工 服务类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
public interface StaffService extends IService<Staff> {

    Result<Page<Staff>> pageList(Integer page, Integer size, String num, String shopId, Integer state);

    Result<?> add(Staff staff);

    Result<?> updateStaff(Staff staff);

    Result<?> delete(String id);

    Result<List<Staff>> simpleListByShopId(String shopId);

    Result<Map<String, Object>> login(String account, String password, Integer perssion);

}
