package com.smileshark;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IDCreate {
    @Test
    public void createID(){
        for(int i=0;i<20;i++){
            System.out.println(IdUtil.simpleUUID());
        }
    }
}
