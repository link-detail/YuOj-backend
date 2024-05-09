package com.liu.yuojbackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.constant.UserConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.model.dto.user.UserAddRequest;
import com.liu.yuojbackend.model.dto.user.UserLoginRequest;
import com.liu.yuojbackend.model.dto.user.UserRegisterRequest;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.vo.LoginUserVO;
import com.liu.yuojbackend.service.UserService;
import com.sun.jndi.ldap.sasl.SaslInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.liu.yuojbackend.constant.UserConstant.DEFAULT_PASSWORD;
import static com.liu.yuojbackend.constant.UserConstant.SALT;

/**
 * @Author 刘渠好
 * @Date 2024-05-07 21:40
 *
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest ==null){
            return new BaseResponse<> (ErrorCode.PARAMS_ERROR);
    }
        //校验参数
        String userAccount = userRegisterRequest.getUserAccount ();
        String userPassword = userRegisterRequest.getUserPassword ();
        String checkPassword = userRegisterRequest.getCheckPassword ();
        if (StringUtils.isAnyBlank (userAccount,userPassword,checkPassword)){
            //            return null;
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"请确认好注册内容!");

        }
        long result = userService.userResister (userAccount, userPassword, checkPassword);
        return ResultUtils.success (result);

    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest){
        //校验
        if (userLoginRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);

        }
        String userAccount = userLoginRequest.getUserAccount ();
        String userPassword = userLoginRequest.getUserPassword ();
        if (StringUtils.isAnyBlank (userAccount,userPassword)){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"请正确填写账号信息!");
        }
        LoginUserVO loginUserVO = userService.userLogin (userAccount, userPassword, httpServletRequest);
        return  ResultUtils.success (loginUserVO);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest servletRequest){
        if (servletRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        //删除登陆态
        boolean b = userService.userLogout (servletRequest);
        return ResultUtils.success (b);

    }

    /**
     * 获取当前用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request){
        if (request==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser (request);
        return ResultUtils.success (userService.getLoginUserVO (loginUser));

    }


    //region 增删查改
    /**
     * 创建用户 --这个操作只允许管理员来执行
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest){
        if (userAddRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }

        User user = new User ();
        //默认密码 11111111
        String md5DigestAsHex = DigestUtils.md5DigestAsHex ((SALT + DEFAULT_PASSWORD).getBytes ());
        user.setUserPassword (md5DigestAsHex);
        BeanUtil.copyProperties (userAddRequest,user);
        //保存到数据库
        boolean save = userService.save (user);
        if (!save){
            throw new BusinessException (ErrorCode.OPERATION_ERROR,"数据库操作失败!");
        }
        return ResultUtils.success (user.getId ());

    }

}
