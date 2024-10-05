package com.liu.yuojbackend.annotation;

import com.liu.yuojbackend.model.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author 刘渠好
 * @Date 2024-05-09 22:49
 * 权限校验注解
 */
@Target (ElementType.METHOD)  //注解定义在方法上
@Retention (RetentionPolicy.RUNTIME)  //注解将在运行时可用
public @interface AuthCheck {

    /**
     * 注解参数
     * 注：下次注解参数名可以直接写value，之后不用写参数=... 更简化一点
     */
    UserRoleEnum[] mustRole() default {};
}
