package com.smileshark.service.imp;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.entity.Delivery;
import com.smileshark.entity.Shop;
import com.smileshark.exception.BusinessException;
import com.smileshark.mapper.DeliveryMapper;
import com.smileshark.mapper.ShopMapper;
import com.smileshark.service.ShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smileshark.utils.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 门店表 服务实现类
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@Service
@RequiredArgsConstructor
public class ShopServiceImp extends ServiceImpl<ShopMapper, Shop> implements ShopService {
    private final ShopMapper shopMapper;
    private final DeliveryMapper deliveryMapper;
    @Override
    public Result<?> addShop(Shop shop) {
        shop.setShopId(IdUtil.simpleUUID());
        if(this.save(shop)){
            return Result.success("添加成功");
        }else {
            throw new BusinessException(ResultCode.ADD_ERROR);
        }
    }

    @Override
    public Result<Page<Shop>> pageList(Integer page, Integer size, String num, Integer state) {
        LambdaQueryChainWrapper<Shop> orderByCreateDateDesc = lambdaQuery().last("order by create_date desc");
        // 模糊匹配
        orderByCreateDateDesc.like(Shop::getShopOrder, StrUtil.globbing(num));
        // 状态匹配
        if(state != null){
            orderByCreateDateDesc.eq(Shop::getState, state);
        }
        return Result.success(orderByCreateDateDesc.page(new Page<>(page, size)));
    }

    @Override
    public Result<List<Shop>> simpleList() {
        return Result.success(lambdaQuery().select(Shop::getShopId,Shop::getName).list());
    }

    @Override
    public Result<Page<Shop>> pageListDineIn(Integer page, Integer size, String shopId, Integer isDineIn) {
        LambdaQueryChainWrapper<Shop> select = lambdaQuery().select(
                Shop::getShopId,
                Shop::getShopOrder,
                Shop::getIsDineIn,
                Shop::getOpeningHoursStart,
                Shop::getOpeningHoursEnd,
                Shop::getName
        );
        if(shopId != null&&!shopId.isEmpty()){
            select.eq(Shop::getShopId, shopId);
        }
        if(isDineIn != null){
            select.eq(Shop::getIsDineIn, isDineIn);
        }
        return Result.success(select.page(new Page<>(page, size)));
    }

    @Override
    public Result<Page<Shop>> pageListTakeOut(Integer page, Integer size, String shopId, Integer isTakeOut) {
        LambdaQueryChainWrapper<Shop> select = lambdaQuery().select(
                Shop::getShopId,
                Shop::getShopOrder,
                Shop::getIsTakeOut,
                Shop::getOpeningHoursStart,
                Shop::getOpeningHoursEnd,
                Shop::getName
        );
        if(shopId != null&&!shopId.isEmpty()){
            select.eq(Shop::getShopId, shopId);
        }
        if(isTakeOut != null){
            select.eq(Shop::getIsDineIn, isTakeOut);
        }
        return Result.success(select.page(new Page<>(page, size)));
    }

    @Override
    public Result<List<Shop>> pageListByRange(Integer page, Integer size, Integer isDineIn, Integer isTakeOut) {
        List<Shop> shops = list();
        for (Shop shop : shops) {
            shop.setDelivery(deliveryMapper.selectOne(new LambdaQueryWrapper<>(Delivery.class).eq(Delivery::getShopId, shop.getShopId())));
        }
        return Result.success(shops);
    }

    @Override
    public Result<Shop> infoById(String id) {
        Shop shop = lambdaQuery().eq(Shop::getShopId, id).one();
        shop.setDelivery(deliveryMapper.selectOne(new LambdaQueryWrapper<>(Delivery.class).eq(Delivery::getShopId, id)));
        return Result.success(shop);
    }
}
