package com.liu.yuojbackend.person.test;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘渠好
 * @since 2024-10-11 20:41
 * 复制工具类
 */
public class MyCopyUtils {
    /**
     * 单体复制
     * @param source 源
     * @param clazz clazz
      */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source==null){
            return null;
        }
        //获取实例
        T obj;
        try {
             obj = clazz.getDeclaredConstructor ().newInstance ();
            //复制
            BeanUtils.copyProperties (source,obj);
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
        return obj;
    }

    /**
     * 多体复制
     * @param source 源
     * @param clazz clazz
     */

    public static <T> List<T> copy(List<?> source,Class<T> clazz){
        if (CollUtil.isEmpty (source)){
            return new ArrayList<> ();
        }
        return source.stream ().map (c->copy (c,clazz)).collect(Collectors.toList ());
    }
}
