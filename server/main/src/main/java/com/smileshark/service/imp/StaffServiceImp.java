package com.smileshark.service.imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Shop;
import com.smileshark.entity.Staff;
import com.smileshark.entity.StaffLoginLog;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.ShopMapper;
import com.smileshark.mapper.StaffLoginLogMapper;
import com.smileshark.mapper.StaffMapper;
import com.smileshark.service.StaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.InfoThreadLocal;
import com.smileshark.utils.StrUtil;
import com.smileshark.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class StaffServiceImp extends ServiceImpl<StaffMapper, Staff> implements StaffService {
    private final ShopMapper shopMapper;
    private final StaffMapper staffMapper;
    private final TokenUtil tokenUtil;
    private final StaffLoginLogMapper staffLoginLogMapper;

    @Override
    public Result<Page<Staff>> pageList(Integer page, Integer size, String num, String shopId, Integer state) {
        // 初始化分页对象
        Page<Staff> staffPage = new Page<>(page, size);
        // 查询
        Page<Staff> info= staffMapper.selectStaffPageList(staffPage, StrUtil.globbing(num), shopId, state);
        return  Result.success(info);
    }

    @Override
    @Transactional
    public Result<?> add(Staff staff) {
        staff.setStaffId(IdUtil.simpleUUID());
        // 保存数据
        if (save(staff)) {
            // 添加对应门店的员工数量
            int shopUpdate = shopMapper.update(new LambdaUpdateWrapper<>(Shop.class).setSql("people_num = people_num + 1").eq(Shop::getShopId, staff.getShopId()));
            if (shopUpdate == 0) {
                throw new RuntimeException("门店更新失败");
            }
            return Result.success(ResultCode.ADD_SUCCESS);
        } else {
            throw new RuntimeException("员工添加失败");
        }
    }

    @Override
    @Transactional
    public Result<?> updateStaff(Staff staff) {
        // 先查询到原有的数据
        Staff oldInfo = lambdaQuery().eq(Staff::getStaffId, staff.getStaffId()).one();
        // 判断门店是否更换
        if (!oldInfo.getShopId().equals(staff.getShopId())) {
            int update = shopMapper.update(new LambdaUpdateWrapper<>(Shop.class).setSql("people_num = people_num - 1").eq(Shop::getShopId, oldInfo.getShopId()));
            int update1 = shopMapper.update(new LambdaUpdateWrapper<>(Shop.class).setSql("people_num = people_num + 1").eq(Shop::getShopId, staff.getShopId()));
            if (update == 0 || update1 == 0) {
                throw new BusinessException(ResultCode.UPDATE_ERROR);
            }
        }
        // 修改数据
        if (!updateById(staff)) {
            throw new BusinessException(ResultCode.UPDATE_ERROR);
        }
        return Result.success(ResultCode.UPDATE_SUCCESS);
    }

    @Override
    public Result<?> delete(String id) {
        // 减少对应门店的员工数量
        int shopUpdate = shopMapper.update(new LambdaUpdateWrapper<>(Shop.class).setSql("people_num = people_num - 1").eq(Shop::getShopId, getById(id).getShopId()));
        if (shopUpdate == 0) {
            throw new BusinessException(ResultCode.DELETE_ERROR);
        }
        // 删除数据
        if (removeById(id)) {
            return Result.success(ResultCode.DELETE_SUCCESS);
        } else {
            throw new BusinessException(ResultCode.DELETE_ERROR);
        }
    }

    @Override
    public Result<List<Staff>> simpleListByShopId(String shopId) {
        return Result.success(lambdaQuery().eq(Staff::getShopId, shopId).list());
    }

    @Override
    public Result<Map<String, Object>> login(String account, String password, Integer perssion) {
        // account可能是账号也可能是phone
        Staff staff = lambdaQuery().eq(Staff::getAccount, account).or().eq(Staff::getPhone, account).one();
        if (staff == null) {
            throw new BusinessException(ResultCode.NOT_HAVE_ACCOUNT);
        }
        if (!staff.getPassword().equals(password)) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        // 验证登录账户的权限
        if (!staff.getPerssion().equals(2)&&!staff.getPerssion().equals(perssion)) {
            throw new BusinessException(ResultCode.PERMISSION_ERROR);
        }
        staff.setPassword(null);
        // 登录成功
        Map<String, Object> map = BeanUtil.beanToMap(staff);
        map.put("token", tokenUtil.createToken(staff));
        // 添加登录的日志
        StaffLoginLog staffLoginLog = new StaffLoginLog();
        staffLoginLog.setStaffId(staff.getStaffId());
        staffLoginLog.setLoginTime(LocalDateTime.now());
        staffLoginLog.setLoginIp(InfoThreadLocal.getRequest().getRemoteAddr());
        staffLoginLogMapper.insert(staffLoginLog);

        return Result.success(map);
    }
}
