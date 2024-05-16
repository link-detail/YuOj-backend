package com.liu.yuojbackend.model.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.liu.yuojbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 22:26
 * 用户列表查询条件请求类
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = 8316486946502211372L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;


    /**
     * 用户简介
     */
    private String userProfile;


    /**
     * 用户角色:user/ admin
     */
    private String userRole;


}
