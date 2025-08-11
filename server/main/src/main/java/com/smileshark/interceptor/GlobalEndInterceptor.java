package com.smileshark.interceptor;

import com.smileshark.code.ResultCode;
import com.smileshark.exception.BusinessException;
import com.smileshark.utils.InfoThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class GlobalEndInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(InfoThreadLocal.getPass().equals(true)){
            return true;
        }
        throw new BusinessException(ResultCode.TOKEN_ERROR);
    }
}
