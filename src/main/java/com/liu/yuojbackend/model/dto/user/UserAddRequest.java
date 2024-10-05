package com.liu.yuojbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-09 22:18
 * 创建用户请求类
 */
@Data
public class UserAddRequest implements Serializable {


    private static final long serialVersionUID = -2090586432976906798L;


    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 头像
     */
    private String userAvatar;


    /**
     * 用户角色:user/ admin
     */
    private String userRole;


}
