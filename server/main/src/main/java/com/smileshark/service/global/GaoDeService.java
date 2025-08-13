package com.smileshark.service.global;

import com.smileshark.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GaoDeService {
    private final RestTemplate restTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    public Result<String> addressToPos(String address) {
        String json = stringRedisTemplate.opsForValue().get("addressToPos:" + address);
        if (json != null) {
            return Result.success(json);
        }
        String forObject = restTemplate.getForObject("https://restapi.amap.com/v3/geocode/geo?address={address}&key={key}", String.class, Map.of("address", address, "key", "88c2a056f3687cf5865981a54a9ec06b"));
        // 可以将数据存入redis中，防止频繁调用
        stringRedisTemplate.opsForValue().set("addressToPos:" + address, forObject);
        return Result.success(forObject);
    }
}
