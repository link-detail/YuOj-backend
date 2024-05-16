package com.liu.yuojbackend.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 22:49
 * sql工具
 */
public class SqlUtils {

    /**
     * 验证排序字段是否合法(防止sql注入)
     */
    public static boolean validSortField(String sortField){
        if (StringUtils.isBlank (sortField)){
            return false;
        }
        return !StringUtils.containsAny (sortField,"=","(",")"," ");

    }
}
