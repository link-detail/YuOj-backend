package com.liu.yuojbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-08 19:52
 * 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    //序列化操作
    private static final long serialVersionUID = 7428029616048248898L;

    private int code;  //返回码

    private T data; //返回数据

    private String message; //返回信息

    public BaseResponse(int code ,T data,String message){
        this.code=code;
        this.data=data;
        this.message=message;
    }

    public BaseResponse(int code,T data){
        this(code,data,"");
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode (),null,errorCode.getMessage ());
    }

}
