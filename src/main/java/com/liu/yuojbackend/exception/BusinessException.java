package com.liu.yuojbackend.exception;

import com.liu.yuojbackend.common.ErrorCode;



/**
 * @Author 刘渠好
 * @Date 2024-05-08 20:41
 * 自定义异常类
 */
public class BusinessException extends RuntimeException{

    /**
     * 错误吗
     */
    private  int code;

    /**
     * code+ message
     */
    public BusinessException(int code,String message){
        super(message);
        this.code=code;
    }

    /**
     * errorCode
     */
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage ());
        this.code=errorCode.getCode ();
    }

    /**
     * errorCode+message
     */
    public BusinessException(ErrorCode errorCode,String message){
        super(message);
        this.code=errorCode.getCode ();
    }


    public  int getCode(){
        return code;
    }


}
