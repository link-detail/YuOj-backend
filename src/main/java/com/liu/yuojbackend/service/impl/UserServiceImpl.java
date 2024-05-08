package com.liu.yuojbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.mapper.UserMapper;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.liu.yuojbackend.constant.UserConstant.SALT;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-05-07 21:23:37
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     * 用户注册
     */
    @Override
    public long userResister(String userAccount,String userPassword,String checkPassword) {

        /**
         * 在查询数据库的时候，先把数据库之外的操作做了，之后再去查询数据库，避免浪费数据库资源
         */
        //1.校验
        if (StringUtils.isAnyBlank (userAccount,userPassword,checkPassword)){
            throw  new BusinessException (ErrorCode.PARAMS_ERROR,"请求参数有误!");
        }
        if (userAccount.length ()<4){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"账号长度过短!");
        }
        if (userPassword.length ()<8||checkPassword.length ()<8){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"密码长度小于八位!");
        }
        // 校验两次输入密码是否一致
        if (!userPassword.equals (checkPassword)){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"两次输入密码不一致!");
        }

        //加锁(避免账号重复)
        synchronized (userAccount.intern ()){

        //账号不可以一致
        QueryWrapper<User> queryWrapper=new QueryWrapper<> ();
        queryWrapper.eq ("userAccount",userAccount);
        User selectOne = this.baseMapper.selectOne (queryWrapper);
        if (selectOne!=null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"该账号已存在!");
        }
        //2.密码进行加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex ((SALT + userPassword).getBytes ());
        //3.插入数据
        User user = new User ();
        user.setUserAccount (userAccount);
        user.setUserPassword (md5DigestAsHex);
        boolean save = this.save (user);
        if (!save){
            throw new BusinessException (ErrorCode.SYSTEM_ERROR,"注册失败，数据库操作出错!");
        }
        return user.getId ();
    }
    }
}




