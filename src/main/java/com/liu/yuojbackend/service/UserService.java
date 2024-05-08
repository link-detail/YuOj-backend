package com.liu.yuojbackend.service;

import com.liu.yuojbackend.model.dto.user.UserRegisterRequest;
import com.liu.yuojbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**

 针对表【user(用户表)】的数据库操作Service
*/
public interface UserService extends IService<User> {

    //用户注册
    long userResister(String userAccount,String userPassword,String checkPassword);
}
