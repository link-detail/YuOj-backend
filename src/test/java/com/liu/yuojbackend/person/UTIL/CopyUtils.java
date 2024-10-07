package com.liu.yuojbackend.person.UTIL;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘渠好
 * @since 2024-10-07 22:58
 * 复制类
 */
public class CopyUtils {
    /**
     * 单体复制
     * @param source 源
     * @param clazz 类
     */
    public static <T> T copy(Object source,  Class<T> clazz) {
        if (source==null){
            return null;
        }
        T obj;
        try {
             obj = clazz.getDeclaredConstructor ().newInstance ();
            //拷贝数据
            BeanUtils.copyProperties (source,obj);
        } catch (Exception e) {
            return null;
        }
        return obj;
    }

    public static <T> List<T> copyList(List<?> list,Class<T> clazz){
        if (CollUtil.isEmpty (list)){
            return new ArrayList<> ();
        }
        return list.stream ().map (c->copy(c,clazz)).collect(Collectors.toList());
    }
}
