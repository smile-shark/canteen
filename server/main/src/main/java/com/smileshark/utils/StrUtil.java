package com.smileshark.utils;

public class StrUtil {
    public static String globbing(String oldStr){
        if(oldStr == null || oldStr.isEmpty())return "%";
        return "%"+String.join("%",oldStr.split(""))+"%";
    }
}
