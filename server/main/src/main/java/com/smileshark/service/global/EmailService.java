package com.smileshark.service.global;

import cn.hutool.core.util.RandomUtil;
import com.smileshark.code.ResultCode;
import com.smileshark.common.Result;
import com.smileshark.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${redis.key.key5min}")
    private String key5min;
    @Value("${redis.key.keyDay}")
    private String keyDay;
    private final StringRedisTemplate stringRedisTemplate;
    private final JavaMailSender javaMailSender;
    public Result<?> sendEmail(String email) {
        // 校验邮箱格式
        if (!email.matches("^\\w+@\\w+\\.\\w+$")) {
            throw new BusinessException(ResultCode.EMAIL_FORMAT_ERROR);
        }


//        // 5分钟校验
//        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key5min + email))
//                && Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(key5min + email))) >= 2) {
//            return Result.error("5分钟内只能发送2次验证码");
//        }
//        // 24小时校验
//        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(keyDay + email))
//                && Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(keyDay + email))) >= 5) {
//            return Result.error("24小时内只能发送5次验证码");
//        }
        // 生成4位验证码
        String code = RandomUtil.randomNumbers(4);
        // 使用邮箱发送验证码
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(sender);
//        message.setTo(email);
//        message.setSubject("验证码");
//        message.setText("您的验证码为：" + code);
//        try {
//            javaMailSender.send(message);
//        } catch (MailException e) {
//            throw new BusinessException(ResultCode.EMAIL_SEND_ERROR);
//        }

        // 在redis中存储5分钟
        stringRedisTemplate.opsForValue()
                .setIfAbsent("code:" + email, code, 5, TimeUnit.MINUTES);

        // 计数并自增
        stringRedisTemplate.opsForValue().increment(key5min + email);
        stringRedisTemplate.expire(key5min + email, 5, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().increment(keyDay + email);
        stringRedisTemplate.expire(keyDay + email, 24, TimeUnit.HOURS);

        return Result.success("验证码发送成功，5分钟有效");
    }

    public boolean verifyCode(String userEmail, String code) {
        // 从redis中获取验证码
        String verif = stringRedisTemplate.opsForValue().get("code:" + userEmail);
        if (verif == null) {
            return false;
        }
        // 校验验证码
        if (!code.equals(verif)) {
            return false;
        }
        // 删除验证码
        stringRedisTemplate.delete("code:" + userEmail);
        return true;
    }
}
