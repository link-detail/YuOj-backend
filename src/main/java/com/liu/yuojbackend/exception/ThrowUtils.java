package com.liu.yuojbackend.exception;

import com.liu.yuojbackend.common.ErrorCode;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 21:40
 * 抛异常工具类
 */
public class ThrowUtils {

    /***
     *
     * @param condition  成立条件
     * @param runtimeException  //异常
     */
    public static void throwIf(boolean condition,RuntimeException runtimeException){
        if (condition){
            throw runtimeException;
        }


    }

    public static void throwIf(boolean condition, ErrorCode errorCode){
        if (condition){
            throwIf (condition,new BusinessException (errorCode));
        }

    }

    public static void throwIf(boolean condition, ErrorCode errorCode,String message){
        if (condition){
            throwIf (condition,new BusinessException (errorCode,message));
        }

    }


}
