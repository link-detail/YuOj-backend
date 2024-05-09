package com.liu.yuojbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-09 19:57
 * 用户登陆请求类
 */
@Data
public class UserLoginRequest implements Serializable {


    //序列化操作
    private static final long serialVersionUID = -6408820556983688422L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
