package com.liu.yuojbackend.common;


/**
 * @Author 刘渠好
 * @Date 2024-05-08 20:10
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<> (0,data,"ok");
    }

    /**
     * 失败 --- errorCode
     */
    public static   BaseResponse  error(ErrorCode errorCode){
        return new BaseResponse<> (errorCode);
    }

    /**
     * 失败 --- code,message
     */
    public static   BaseResponse  error(int code, String message){
        return new BaseResponse<> (code,null,message);
    }

    /**
     * 失败 ---errorCode+message
     */
    public static   BaseResponse  error(ErrorCode errorCode,String message){
        return new BaseResponse<> (errorCode.getCode (),null,message);
    }

}
