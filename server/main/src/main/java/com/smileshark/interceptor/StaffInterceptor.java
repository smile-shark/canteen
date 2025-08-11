package com.smileshark.interceptor;

import com.smileshark.entity.Customer;
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
public class StaffInterceptor implements HandlerInterceptor {
    private final TokenUtil tokenUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("StaffToken");
        Staff staff = tokenUtil.parseToken(token, Staff.class);
        if(staff!=null){
            // 身份验证成功
            InfoThreadLocal.setStaff(staff);
            InfoThreadLocal.setPass(true);
        }
        return true;
    }
}