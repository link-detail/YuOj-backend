package com.liu.yuojbackend.model.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举类
 */
public enum UserRoleEnum {

    ADMIN("管理员","admin"),
    USER("普通用户","user"),
    BAN("黑名单用户","ban")
    ;

    private String text;

    private String value;

    UserRoleEnum(String text,String value){
        this.text=text;
        this.value=value;
    }

    //根据text查找
    public static UserRoleEnum getEnumByValue(String value){
        //判断参数是否为空
        if (ObjectUtil.isEmpty (value)){
            return null;
        }
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values ()) {
            if (userRoleEnum.getValue ().equals (value)){
                return userRoleEnum;
            }
        }
        return null;
    }

    //获取值列表
    public static List<String> getValues(){
        return Arrays.stream (values ()).map (item -> item.value).collect(Collectors.toList());

    }

    public String getText(){
        return text;
    }

    public String getValue(){
        return value;
    }
}
