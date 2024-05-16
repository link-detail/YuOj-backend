package com.liu.yuojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.yuojbackend.model.dto.user.UserQueryRequest;
import com.liu.yuojbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.yuojbackend.model.vo.LoginUserVO;
import com.liu.yuojbackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**

 针对表【user(用户表)】的数据库操作Service
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    long userResister(String userAccount,String userPassword,String checkPassword);

    /**
     *用户登陆
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     * 用户注销
     */
    boolean userLogout(HttpServletRequest servletRequest);

    /**
     * 获取当前登陆用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏之后的数据
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取用户包装类 UserVO
     */
    UserVO getUserVO(User user);

    /**
     * 根据条件查询列表用户 wrapper
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

}
