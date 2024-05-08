package com.liu.yuojbackend.common;

/**
 * @Author 刘渠好
 * @Date 2024-05-08 19:58
 * 自定义错误吗
 */
@SuppressWarnings ("all")
public enum ErrorCode {

    SUCCESS(0,"OK" ),
    PARAMS_ERROR(40000,"请求参数错误"),
    NOT_LOGIN_ERROR(40001,"未登录"),
    NO_AUTH_ERROR(40002,"无权限"),
    NOT_FOUND_ERROR(40003,"请求数据不存在"),
    FORBIDDEN_ERROR(40004,"禁止访问"),
    SYSTEM_ERROR(40005,"系统错误"),
    OPERATION_ERROR(40006,"操作失败");


    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    /**
     *构造函数
     */
     ErrorCode(int code, String message){
        this.code=code;
        this.message=message;
    }

    public int getCode(){
         return code;
    }

    public String getMessage()
    {
        return message;
    }

}
