package com.smileshark.utils;

public class RedisKeyUtils {
    public static String formatKey(String ...args){
        return String.join(":", args);
    }
}
