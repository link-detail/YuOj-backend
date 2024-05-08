package com.liu.yuojbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-08 20:14
 * 用户注册请求类
 */
@Data
public class UserRegisterRequest implements Serializable {


    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;


    /**
     * 再次校验密码
     */
    private String checkPassword;


    private static final long serialVersionUID = 3935889196688664989L;
}
