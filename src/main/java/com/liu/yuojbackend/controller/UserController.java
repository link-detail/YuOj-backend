package com.liu.yuojbackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.DeleteRequest;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.constant.UserConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.model.dto.user.*;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.model.vo.LoginUserVO;
import com.liu.yuojbackend.model.vo.UserVO;
import com.liu.yuojbackend.service.UserService;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import com.sun.org.apache.bcel.internal.generic.NEW;
import kotlin.math.MathKt;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.nio.channels.Pipe;
import java.util.BitSet;
import java.util.List;

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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return new BaseResponse<> (ErrorCode.PARAMS_ERROR);
        }
        //校验参数
        String userAccount = userRegisterRequest.getUserAccount ();
        String userPassword = userRegisterRequest.getUserPassword ();
        String checkPassword = userRegisterRequest.getCheckPassword ();
        if (StringUtils.isAnyBlank (userAccount, userPassword, checkPassword)) {
            //            return null;
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "请确认好注册内容!");

        }
        long result = userService.userResister (userAccount, userPassword, checkPassword);
        return ResultUtils.success (result);

    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        //校验
        if (userLoginRequest == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);

        }
        String userAccount = userLoginRequest.getUserAccount ();
        String userPassword = userLoginRequest.getUserPassword ();
        if (StringUtils.isAnyBlank (userAccount, userPassword)) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "请正确填写账号信息!");
        }
        LoginUserVO loginUserVO = userService.userLogin (userAccount, userPassword, httpServletRequest);
        return ResultUtils.success (loginUserVO);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest servletRequest) {
        if (servletRequest == null) {
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
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        if (request == null) {
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
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }

        User user = new User ();
        //默认密码 11111111
        String md5DigestAsHex = DigestUtils.md5DigestAsHex ((SALT + DEFAULT_PASSWORD).getBytes ());
        user.setUserPassword (md5DigestAsHex);
        BeanUtil.copyProperties (userAddRequest, user);
        //保存到数据库
        boolean save = userService.save (user);
        if (!save) {
            throw new BusinessException (ErrorCode.OPERATION_ERROR, "数据库操作失败!");
        }
        return ResultUtils.success (user.getId ());

    }

    /**
     * 删除用户-----仅是管理员权限可以删除
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        //校验参数
        if (deleteRequest == null || deleteRequest.getId () <= 0) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success (userService.removeById (deleteRequest.getId ()));

    }

    /**
     * 更新用户------仅是管理员权限可以修改
     */
    @PostMapping("/updateUser")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {

        if (userUpdateRequest == null || userUpdateRequest.getId () <= 0) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);

        }
        User user = new User ();
        BeanUtil.copyProperties (userUpdateRequest,user);
        boolean result = userService.updateById (user);

        ThrowUtils.throwIf (!result,ErrorCode.SYSTEM_ERROR,"数据库操作失败");

        return ResultUtils.success (result);
    }

    /**
     * 根据id获取未脱敏的用户-------仅是管理员可以查看用户的完整信息
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(@RequestParam long id){
        if (id<=0){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        User byId = userService.getById (id);
        ThrowUtils.throwIf (byId==null,ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success (byId);
    }

    /**
     * 根据id获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(@RequestParam long id){
        //校验
        if (id<=0){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        //可以直接复用上面的方法
        BaseResponse<User> userBaseResponse = getUserById (id);
        User user = userBaseResponse.getData ();
        return ResultUtils.success (userService.getUserVO (user));
    }

    /**
     * 分页获取用户列表----仅是管理员可以操作
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest){

        long current = userQueryRequest.getCurrent ();
        long pageSize = userQueryRequest.getPageSize ();
        Page<User> page = userService.page (new Page<> (current, pageSize),
                userService.getQueryWrapper (userQueryRequest));
        return ResultUtils.success (page);

    }

    /**
     * 分页获取用户封装列表
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest){

        //校验
        if (userQueryRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        //获取分页数据
        long current = userQueryRequest.getCurrent ();
        long pageSize = userQueryRequest.getPageSize ();

        //限制爬虫
        ThrowUtils.throwIf (pageSize>20,ErrorCode.PARAMS_ERROR,"你的查询有误！");
        Page<User> page = userService.page (new Page<> (current, pageSize),
                userService.getQueryWrapper (userQueryRequest));

        List<UserVO> userVOList = userService.getUserVOList (page.getRecords ());
        Page<UserVO> userVOPage = new Page<> (current,pageSize,page.getTotal ());
        return ResultUtils.success (userVOPage.setRecords (userVOList));

    }

    /**
     * 更新个人信息
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,HttpServletRequest request){
        //校验参数
        if (userUpdateMyRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }

        boolean result = userService.updateMyUser (userUpdateMyRequest,request);

        return ResultUtils.success (result);


    }
}
