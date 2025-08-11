package com.smileshark.interceptor;

import com.smileshark.entity.Merchant;
import com.smileshark.entity.Staff;
import com.smileshark.utils.InfoThreadLocal;
import com.smileshark.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MerchantInterceptor implements HandlerInterceptor {
    public final TokenUtil tokenUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("MerchantToken");
        Merchant merchant = tokenUtil.parseToken(token, Merchant.class);
        if(merchant!=null){
            // 身份验证成功
            InfoThreadLocal.setMerchant(merchant);
            InfoThreadLocal.setPass(true);
        }
        return true;
    }
}
