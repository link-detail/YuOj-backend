package com.liu.yuojbackend.model.dto.user;

import com.liu.yuojbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 22:26
 * 用户列表查询条件请求类
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)  //这个注解的意思是继承父类的字类，如果callSuper=true，则表示继承的属性也要一致才返回true
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
