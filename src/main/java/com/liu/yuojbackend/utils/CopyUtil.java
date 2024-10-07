package com.liu.yuojbackend.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘渠好
 * @since 2024-10-05 21:49
 * 复制工具类
 */
public class CopyUtil {
    /**
     * 单体复制
     * @param source 源
     * @param clazz clazz
     * @return {@link T}
     */

    public static <T> T copy(Object source, Class<T> clazz) {
        if (source==null){
            return  null;
        }
        T obj;
        try {
            //获取这个类的实例
            obj = clazz.getDeclaredConstructor ().newInstance ();
        } catch (Exception e) {
            return null;
        }
        BeanUtil.copyProperties (source,obj);
        return obj;
    }

    /**
     * 列表复制
     * @param source 源
     * @param clazz clazz
     * @return {@link List}<{@link List}>
     */
    public static <T> List<T>  copyList(List<?> source,Class<T> clazz) {
        if (source == null) {
            return null;
        }
        return source.stream ().map (c->copy(c,clazz)).collect(Collectors.toList());
    }

}


























