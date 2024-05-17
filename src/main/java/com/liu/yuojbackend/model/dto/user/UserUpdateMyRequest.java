package com.liu.yuojbackend.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-17 23:03
 * 用户自我更新请求类
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 新密码密码
     */
    private String userPassword;

    /**
     * 检查新密码
     */
    private String checkPassword;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
