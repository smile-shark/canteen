package com.smileshark.config;

import com.smileshark.interceptor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final GlobalStartInterceptor globalStartInterceptor;
    private final CustomerInterceptor customerInterceptor;
    private final StaffInterceptor staffInterceptor;
    private final MerchantInterceptor merchantInterceptor;
    private final GlobalEndInterceptor globalEndInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalStartInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(customerInterceptor)
                .addPathPatterns(
                        "/cuisine/cuisineServiceList",
                        "/cuisine/getSimpleCuisineById",
                        "/cuisineType/list",
                        "/customerOrder/dineInOrder",
                        "/customerOrder/placeOrderGet",
                        "/customerOrder/placeOrderNow",
                        "/customerOrder/payOrder",
                        "/customerOrder/takeOutAndDineInOrder",
                        "/delivery/infoById",
                        "/diningTable/infoById",
                        "/discountCoupon/usableCoupon",
                        "/discountCouponCustomer/list",
                        "/shop/infoById",
                        "/shop/pageListByRange",
                        "/wallet/infoById"
                );
        registry.addInterceptor(staffInterceptor);
        registry.addInterceptor(merchantInterceptor)
                .addPathPatterns(
                        "/area/infoByAreaId",
                        "/area/listById",
                        "/cuisine/add",
                        "/cuisine/delete",
                        "/cuisine/infoById",
                        "/cuisine/inventoryPageList",
                        "/cuisine/pageList",
                        "/cuisine/simpleListByShopId",
                        "/cuisine/update",
                        "/cuisineComponent/add",
                        "/cuisineComponent/delete",
                        "/cuisineComponent/list",
                        "/cuisineType/add",
                        "/cuisineType/delete",
                        "/cuisineType/list",
                        "/customer/pageList",
                        "/delivery/infoById",
                        "/delivery/saveOrUpdate",
                        "/discountCoupon/add",
                        "/discountCoupon/pageList",
                        "/discountCoupon/sendCoupon",
                        "/discountCoupon/sendCouponList",
                        "/diningTable/add",
                        "/diningTable/delete",
                        "/diningTable/pageList",
                        "/diningTable/update",
                        "/diningTableType/delete",
                        "/diningTableType/list",
                        "/diningTableType/update",
                        "/province/list",
                        "/rawMaterial/add",
                        "/rawMaterial/delete",
                        "/rawMaterial/pageList",
                        "/rawMaterial/update",
                        "/rawMaterialType/add",
                        "/rawMaterialType/delete",
                        "/rawMaterialType/list",
                        "/shop/add",
                        "/shop/delete",
                        "/shop/pageList",
                        "/shop/pageListDineIn",
                        "/shop/pageListTakeOut",
                        "/shop/simpleList",
                        "/shop/update",
                        "/staff/add",
                        "/staff/delete",
                        "/staff/pageList",
                        "/staff/simpleListByShopId",
                        "/staff/update",
                        "/staffLoginLog/pageList",
                        "/styleOfCooking/add",
                        "/styleOfCooking/delete",
                        "/styleOfCooking/list",
                        "/taste/add",
                        "/taste/delete",
                        "/taste/list",
                        "/workingSchedule/add",
                        "/workingSchedule/pageList"
                );
        registry.addInterceptor(globalEndInterceptor)
                .addPathPatterns("/**").excludePathPatterns(
                        "/staff/login",
                        "/merchant/login",
                        "/customer/register",
                        "/customer/passwordLogin",
                        "/customer/verifyCodeLogin",
                        "/global/sendEmail",
                        "/global/addressToPos"
                );
    }
}
