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
     *
     * 根据排序字段恶意注入
     * 一般：前端传入参数 {prop:"id",order:"desc"}  //根据id字段降序   select * from table1 order by id desc
     * sql恶意注入: 前端传入参数 {prop:"id",order:";delete from table1"}  select * from table1; delete from table1
     * 这样就会造成我们的表被恶意删除
     */
    public static boolean validSortField(String sortField){
        if (StringUtils.isBlank (sortField)){
            return false;
        }
        return !StringUtils.containsAny (sortField,"=","(",")"," ",";");

    }

}
