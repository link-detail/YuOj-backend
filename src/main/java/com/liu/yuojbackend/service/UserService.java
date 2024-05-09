package com.liu.yuojbackend.service;

import com.liu.yuojbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.yuojbackend.model.vo.LoginUserVO;

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

}
