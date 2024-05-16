package com.liu.yuojbackend.person.exception;

import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 23:31
 * 抛异常工具类
 */
public class ThrowUtil {

    /**
     *
     * @param condition  条件
     * @param runtimeException  异常
     */
    public static void throwIf(boolean condition,RuntimeException runtimeException){
        if (condition){
            throw runtimeException;
        }

    }

    public static void throwIf(boolean condition, ErrorCode errorCode){

            throwIf (condition,new BusinessException (errorCode));

    }


    public static void throwIf(boolean condition, ErrorCode errorCode,String message){

            throwIf (condition,new BusinessException (errorCode,message));

    }
}
