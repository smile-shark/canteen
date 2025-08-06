package com.smileshark.utils;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.smileshark.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${token.key}")
    private String key;
    @Value("${token.effective-time.short-m}")
    private Integer tokenShortTime_m;
    @Value("${token.effective-time.long-h}")
    private Integer tokenLongTime_h;

    public <T> String createToken(T o) {
        Class<?> aClass = o.getClass();
        String info = JSONUtil.toJsonStr(o);
        String shortToken = createTimeLimitedToken(info, tokenShortTime_m * 60 * 1000);
        String longToken = createTimeLimitedToken(info, tokenLongTime_h * 60 * 60 * 1000);
        stringRedisTemplate.opsForValue()
                .set(shortToken, longToken, tokenShortTime_m, TimeUnit.MINUTES);

        return shortToken;
    }

    public <T> boolean parseToken(String token, Class<T> clazz) {
        if (token == null) {
            return false;
        }
        // 验证令牌有效性
        if (!JWTUtil.verify(token, key.getBytes())) {
            return false;
        }
        JWT jwt = JWTUtil.parseToken(token);
        // 验证令牌过期时间
        NumberWithFormat payload = (NumberWithFormat) jwt.getPayload("exp");
        Date expDate = DateUtil.date(payload.longValue());
        // 如果还有一分钟就过期
        if (DateUtil.between(DateUtil.date(), expDate, DateUnit.MINUTE) < 1) {
            // 获取长时间令牌
            String longToken = stringRedisTemplate.opsForValue()
                    .get(token);
            if (longToken == null) {
                return false;
            }
            // 如果长时间令牌没有失效才创建新的短时间令牌
            try {
                JWTValidator.of(longToken).validateDate();
            } catch (ValidateException e) {
                return false;
            }
            String newShortToken = jwt.setExpiresAt(DateUtil.offsetMinute(expDate, tokenShortTime_m)).sign();
            stringRedisTemplate.opsForValue()
                    .set(newShortToken, token, tokenShortTime_m, TimeUnit.MINUTES);
            InfoThreadLocal.getResponse().setHeader("Authorization", newShortToken);
            // 删除旧的令牌数据
            stringRedisTemplate.delete(token);
        }
        InfoThreadLocal.setCustomer((Customer) JSONUtil.toBean((String) jwt.getPayload("info"), clazz));
        return true;
    }

    private String createTimeLimitedToken(String info, Integer time) {
        return JWT.create().setPayload("info", info)
                .setIssuedAt(DateUtil.date())// 设置签发时间
                .setNotBefore(DateUtil.date())// 生效时间
                .setExpiresAt(DateUtil.date(System.currentTimeMillis() + time))// 设置过期时间
                .setKey(key.getBytes())
                .sign();
    }

}
