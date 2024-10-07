package com.liu.yuojbackend.model.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举类
 */
@Getter
public enum UserRoleEnum {

    ADMIN ("管理员", "admin"),
    USER ("普通用户", "user"),
    BAN ("黑名单用户", "ban");

    private String text;

    /**
     * 注解解释：
     * @EnumValue 用于指定枚举值在数据库中对应的值。当你使用MyBatis-Plus查询数据库并将结果映射到枚举类型时，它会查找这个注解指定的字段值，并将其转换为枚举类型。
     * {
     * 大白话意思：mybatis-plus会根据表中字段的值来查询对应的枚举值，然后将枚举值映射到实体类中对应属性，这个枚举值是根据@EnumValue注解下的属性来找的
     * 反之，实体类对象存入数据中，该属性的值，是EnumValue注解下的值：主要用在数据库表和实体类的结果映射
     * }
     * @JsonValue 用于指定枚举和JSON值的映射：主要用在显示前端页面
     */
    @EnumValue
    @JsonValue
    private String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    //根据text查找
    public static UserRoleEnum getEnumByValue(String value) {
        //判断参数是否为空
        if (ObjectUtil.isEmpty (value)) {
            return null;
        }
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values ()) {
            if (userRoleEnum.getValue ().equals (value)) {
                return userRoleEnum;
            }
        }
        return null;
    }

    //获取值列表
    public static List<String> getValues() {
        return Arrays.stream (values ()).map (item -> item.value).collect (Collectors.toList ());

    }

}
