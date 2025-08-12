package com.smileshark.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSocketMessage{
    // 父类中有token和桌位号
    private String cuisineId; // 菜品id
    private Boolean isAdd; // 是否是新增菜品，否则减少
    private String token;
    private String diningTableId;
}
