package com.liu.yuojbackend.person.UTIL;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘渠好
 * @since 2024-10-08 21:14
 * 复制工具类
 */
public class CopyUtils {
    /**
     * 单体复制
     * @param source 源
     * @param clazz clazz
     */

    public static <T> T copy(Object source ,Class<T> clazz){
        if (source==null){
            return null;
        }
        //获取类的实例化对象
        T obj;
        try {
            obj = clazz.getDeclaredConstructor ().newInstance ();
            //复制拷贝数据
            BeanUtils.copyProperties (source,clazz);
        } catch (Exception e) {
            return null;
        }

        return obj;

    }

    /**
     * 多行复制
     * @param source 源码
     * @param clazz clazz
     */
    public static <T> List<T> copyList(List<?> source,Class<T> clazz){
        if (CollUtil.isEmpty (source)){
            return new ArrayList<> ();
        }
       return source.stream ().map (c->copy(c,clazz)).collect(Collectors.toList());
    }
}
