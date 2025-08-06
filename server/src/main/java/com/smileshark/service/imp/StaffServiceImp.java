package com.smileshark.service.imp;

import com.smileshark.entity.Staff;
import com.smileshark.mapper.StaffMapper;
import com.smileshark.service.StaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月06日
 */
@Service
public class StaffServiceImp extends ServiceImpl<StaffMapper, Staff> implements StaffService {

}
