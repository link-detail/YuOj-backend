package com.liu.yuojbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 21:31
 * 更新用户请求类
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 6378338447391592299L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名字
     */
    private String userName;


    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 用户头像
     */
    private String userAvatar;
}

