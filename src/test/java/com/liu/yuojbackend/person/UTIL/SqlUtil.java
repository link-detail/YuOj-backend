package com.liu.yuojbackend.person.UTIL;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author 刘渠好
 * @Date 2024-05-17 22:35
 */
public class SqlUtil {

    /**
     * 防止sql注入
     */
    public static boolean vaild(String sortFiled){
        if (StringUtils.isBlank (sortFiled)){
            return false;
        }
        //判断sql语句中是否含有 sql注入
        boolean b = StringUtils.containsAny (sortFiled, "(", ")", ";", "=");
        return b;
    }
}
