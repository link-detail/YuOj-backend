package com.liu.yuojbackend.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.DeleteRequest;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.model.dto.user.*;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.model.vo.user.LoginUserVO;
import com.liu.yuojbackend.model.vo.user.UserVO;
import com.liu.yuojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.liu.yuojbackend.service.impl.UserServiceImpl.SALT;


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

    //账号最小长度
    public static final int ACCOUNT_MIN_SIZE=4;

    //密码最小长度
    public static final int PASSWORD_MIN_SIZE=8;
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return new BaseResponse<> (ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount ();
        String userPassword = userRegisterRequest.getUserPassword ();
        String checkPassword = userRegisterRequest.getCheckPassword ();
        //1.校验（这里不建议service层去以做校验，service层主要是处理业务的）
        if (StringUtils.isAnyBlank (userAccount, userPassword, checkPassword)) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "请求参数有误!");
        }
        if (userAccount.length () < ACCOUNT_MIN_SIZE) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "账号长度过短!");
        }
        if (userPassword.length () < PASSWORD_MIN_SIZE || checkPassword.length () < PASSWORD_MIN_SIZE) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "密码长度小于八位!");
        }
        // 校验两次输入密码是否一致
        if (!userPassword.equals (checkPassword)) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "两次输入密码不一致!");
        }

        long result = userService.userResister (userAccount, userPassword, checkPassword);
        return ResultUtils.success (result);

    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpSession session) {
        //校验
        if (userLoginRequest == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);

        }
        String userAccount = userLoginRequest.getUserAccount ();
        String userPassword = userLoginRequest.getUserPassword ();
        //1.校验参数
        if (StringUtils.isAnyBlank (userAccount, userPassword)) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "参数为空!");
        }
        if (userAccount.length ()<ACCOUNT_MIN_SIZE) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"账号错误!");
        }
        if (userPassword.length ()<PASSWORD_MIN_SIZE) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"密码错误!");
        }
        LoginUserVO loginUserVO = userService.userLogin (userAccount, userPassword, session);
        return ResultUtils.success (loginUserVO);
    }

    /**
     * 用户注销
     * @param session 会话
      */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpSession session) {
        if (session == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        //删除登陆态
        boolean b = userService.userLogout (session);
        return ResultUtils.success (b);

    }

    /**
     * 获取当前用户
     * @param session 会话
     * @return 登录用户信息
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpSession session) {
        if (session == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser (session);
        return ResultUtils.success (userService.getLoginUserVO (loginUser));

    }


    //region 增删查改

    /**
     * 创建用户 --这个操作只允许管理员来执行
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }

        User user = new User ();
        //设置用户默认密码 11111111
        String userPassword = "11111111";
        String md5DigestAsHex = DigestUtils.md5DigestAsHex ((SALT + userPassword).getBytes ());
        user.setUserPassword (md5DigestAsHex);
        BeanUtils.copyProperties (userAddRequest, user);
        user.setUserRole (UserRoleEnum.getEnumByValue (userAddRequest.getUserRole ()));
        //保存到数据库
        if (!userService.save (user)) {
            throw new BusinessException (ErrorCode.OPERATION_ERROR, "数据库操作失败!");
        }
        return ResultUtils.success (user.getId ());

    }

    /**
     * 删除用户-----仅是管理员权限可以删除
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
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
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {

        if (userUpdateRequest == null || userUpdateRequest.getId () <= 0) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        User user = new User ();
        BeanUtils.copyProperties (userUpdateRequest,user);
        //设置userRole枚举值
        user.setUserRole (UserRoleEnum.getEnumByValue (userUpdateRequest.getUserRole ()));
        ThrowUtils.throwIf (!userService.updateById (user),ErrorCode.SYSTEM_ERROR,"数据库操作失败");
        return ResultUtils.success (true);
    }

    /**
     * 根据id获取未脱敏的用户-------仅是管理员可以查看用户的完整信息
     */
    @GetMapping("/get")
    @AuthCheck(mustRole=UserRoleEnum.ADMIN)
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
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
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
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
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
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,HttpSession session){
        //校验参数
        if (userUpdateMyRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"请选择一个需要修改的参数！");
        }
        //获取当前登录用户
        User loginUser = userService.getLoginUser (session);
        User user = new User ();
        BeanUtils.copyProperties (userUpdateMyRequest, user);
        user.setId (loginUser.getId ());
        //修改
        boolean result = userService.updateById (user);
        if (!result){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"更新个人信息失败！");
        }
        return ResultUtils.success (true);

    }
}
