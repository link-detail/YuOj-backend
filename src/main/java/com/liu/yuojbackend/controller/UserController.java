package com.liu.yuojbackend.controller;

import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.model.dto.user.UserRegisterRequest;
import com.liu.yuojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
}
