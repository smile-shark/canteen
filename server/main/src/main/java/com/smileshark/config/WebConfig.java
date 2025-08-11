package com.smileshark.config;

import com.smileshark.interceptor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
                .addPathPatterns("/customerOrder/takeOutAndDineInOrder");
        registry.addInterceptor(staffInterceptor);
        registry.addInterceptor(merchantInterceptor);
        registry.addInterceptor(globalEndInterceptor)
                .addPathPatterns("/**");
    }
}
