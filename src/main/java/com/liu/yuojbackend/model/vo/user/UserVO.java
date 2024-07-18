package com.liu.yuojbackend.model.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 22:05
 * 用户包装类
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

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
     * 用户角色:user/ admin
     */
    private String userRole;


    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1502064965786010161L;
}
