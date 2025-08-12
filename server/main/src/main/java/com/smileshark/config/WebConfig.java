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
                        "/customerOrder/takeOutAndDineInOrder",
                        "/shop/pageListByRange",
                        "/shop/infoById",
                        "/cuisineType/list",
                        "/cuisine/cuisineServiceList",
                        "/diningTable/infoById",
                        "/wallet/infoById"
                );
        registry.addInterceptor(staffInterceptor);
        registry.addInterceptor(merchantInterceptor);
        registry.addInterceptor(globalEndInterceptor)
                .addPathPatterns("/**").excludePathPatterns(
                        "/customer/passwordLogin",
                        "/staff/login",
                        "/customer/register",
                        "/customer/verifyCodeLogin",
                        "merchant/login",
                        "/global/sendEmail"
                );
    }
}
