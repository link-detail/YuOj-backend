package com.liu.yuojbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.YuOjBackendApplication;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.constant.CommonConstant;
import com.liu.yuojbackend.constant.UserConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.mapper.UserMapper;
import com.liu.yuojbackend.model.dto.user.UserQueryRequest;
import com.liu.yuojbackend.model.dto.user.UserUpdateMyRequest;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.model.vo.LoginUserVO;
import com.liu.yuojbackend.model.vo.UserVO;
import com.liu.yuojbackend.service.UserService;
import com.liu.yuojbackend.utils.SqlUtils;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
import jdk.management.resource.NotifyingMeter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.Offset;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.PushBuilder;

import java.lang.management.MemoryUsage;
import java.util.*;
import java.util.stream.Collectors;

import static com.liu.yuojbackend.constant.UserConstant.SALT;
import static com.liu.yuojbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-05-07 21:23:37
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     * 用户注册
     */
    @Override
    public long userResister(String userAccount, String userPassword, String checkPassword) {

        /**
         * 在查询数据库的时候，先把数据库之外的操作做了，之后再去查询数据库，避免浪费数据库资源
         */
        //1.校验
        if (StringUtils.isAnyBlank (userAccount, userPassword, checkPassword)) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "请求参数有误!");
        }
        if (userAccount.length () < 4) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "账号长度过短!");
        }
        if (userPassword.length () < 8 || checkPassword.length () < 8) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "密码长度小于八位!");
        }
        // 校验两次输入密码是否一致
        if (!userPassword.equals (checkPassword)) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "两次输入密码不一致!");
        }

        //加锁(避免账号重复)
        synchronized (userAccount.intern ()) {

            //账号不可以一致
            QueryWrapper<User> queryWrapper = new QueryWrapper<> ();
            queryWrapper.eq ("userAccount", userAccount);
            User selectOne = this.baseMapper.selectOne (queryWrapper);

            //判断用户是否已存在
            if (selectOne != null) {
                throw new BusinessException (ErrorCode.PARAMS_ERROR, "该账号已存在!");
            }
            //2.密码进行加密
            String md5DigestAsHex = DigestUtils.md5DigestAsHex ((SALT + userPassword).getBytes ());
            //3.插入数据
            User user = new User ();
            user.setUserAccount (userAccount);
            user.setUserPassword (md5DigestAsHex);
            boolean save = this.save (user);
            if (!save) {
                throw new BusinessException (ErrorCode.SYSTEM_ERROR, "注册失败，数据库操作出错!");
            }
            return user.getId ();
        }
    }

    /**
     * 用户登陆
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        //校验参数
        if (StringUtils.isAnyBlank (userAccount, userPassword)) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "请正确填写账号信息!");
        }
        //1.判断用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<> ();
        //密码是明文的，需要加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex ((SALT + userPassword).getBytes ());
        queryWrapper.eq ("userAccount", userAccount);
        queryWrapper.eq ("userPassword", md5DigestAsHex);
        User user = this.baseMapper.selectOne (queryWrapper);
        if (user == null) {
            log.info ("user login failed,userAccount cannot match userPassword");
            throw new BusinessException (ErrorCode.NOT_FOUND_ERROR, "账号或者密码错误!");
        }
        //2.判断该用户是否被加入黑名单
        if (user.getUserRole ().equals (UserRoleEnum.BAN.getValue ()) ){
            throw new BusinessException (ErrorCode.FORBIDDEN_ERROR, "该用户已经被加入黑名单,请合法使用账号!");
        }
        //3.将用户信息存储在session中
        HttpSession session = httpServletRequest.getSession ();
        session.setAttribute (USER_LOGIN_STATE, user);
        //4.返回信息(脱敏之后的数据)
        return  this.getLoginUserVO (user);

    }

    /**
     * 用户注销
     */
    @Override
    public boolean userLogout(HttpServletRequest servletRequest) {
        //判断是都登陆
        if (servletRequest.getSession ().getAttribute (USER_LOGIN_STATE)==null){
            throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR,"未登录!");
        }
        //移除登录态
        servletRequest.getSession ().removeAttribute (USER_LOGIN_STATE);
        return true;


    }

    /**
     * 获取当前登录用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        User loginUser = (User)request.getSession ().getAttribute (USER_LOGIN_STATE);
        // 先判断是否登录
        if (loginUser == null|| loginUser.getId () == null){
            throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR,"尚未登录!");
        }
        // 从数据库查询 (todo 追求性能的话 可以直接走缓存(redis))
        Long id = loginUser.getId ();
        loginUser = this.getById (id);
        if (loginUser==null){
            throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR);
        }
        return loginUser;
    }


    /**
     * 提取一个共用方法  User --->LoginUserVO
     */

    public LoginUserVO getLoginUserVO(User user){
        if (user==null){
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO ();
        BeanUtil.copyProperties (user,loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取用户包装类
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user==null){
            return null;
        }
        UserVO userVO = new UserVO ();
        BeanUtil.copyProperties (user,userVO);
        return userVO;
    }

    /**
     * 根据条件查询用户列表
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"请求参数为空!");
        }
        Long id = userQueryRequest.getId ();
        String userName = userQueryRequest.getUserName ();
        String userProfile = userQueryRequest.getUserProfile ();
        String userRole = userQueryRequest.getUserRole ();
        QueryWrapper<User> queryWrapper = new QueryWrapper<> ();
        //条件查询

        queryWrapper.eq (id!=null,"id",id);
        queryWrapper.like (StringUtils.isNotBlank (userName),"userName",userName);
        queryWrapper.like (StringUtils.isNotBlank (userProfile),"userProfile",userProfile);
        queryWrapper.eq (StringUtils.isNotBlank (userRole),"userRole",userRole);


        String sortField = userQueryRequest.getSortField ();
        String sortOrder = userQueryRequest.getSortOrder ();

        //分页(必须排序字段满足指定要求，才可以排序)
        queryWrapper.orderBy (SqlUtils.validSortField (sortField),sortOrder.equals (CommonConstant.SORT_ORDER_ASC),sortField);
        return queryWrapper;





    }

    /**
     *用户列表转为用户封装列表
     */
    @Override
    public List<UserVO> getUserVOList(List<User> list) {
        if (CollUtil.isEmpty (list)){
            return new ArrayList<> ();
        }
//        List<UserVO> userVOS = new ArrayList<> ();
//        for (User user : list) {
//            userVOS.add (getUserVO (user));
//        }
//        return userVOS;

        return list.stream ().map (this::getUserVO).collect(Collectors.toList());
    }

    /**
     * 更新个人信息
     * @param userUpdateMyRequest
     * @return
     */
    @Override
    public boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest,HttpServletRequest request) {
        if (userUpdateMyRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"参数有误!");
        }

        //获取登录用户信息
        User loginUser = this.getLoginUser (request);
        Long id = loginUser.getId ();

        String userName = userUpdateMyRequest.getUserName ();
        String userAvatar = userUpdateMyRequest.getUserAvatar ();
        String userProfile = userUpdateMyRequest.getUserProfile ();
        Integer gender = userUpdateMyRequest.getGender ();


        //校验参数
        if (StringUtils.isNotBlank (userName)&&userName.length ()>8){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"用户名字过长!");
        }
        if (StringUtils.isNotBlank (userProfile)&&userProfile.length ()>100){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"用户简介过长!");
        }

        String userPassword = userUpdateMyRequest.getUserPassword ();
        String checkPassword = userUpdateMyRequest.getCheckPassword ();

        //更改密码(小技巧 isEmpty主要来验证该字符串是否未null)
        if (StringUtils.isEmpty (userPassword)||StringUtils.isEmpty (checkPassword)){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"密码不能为null!");
        }
        if (userPassword.length ()<8||checkPassword.length ()<8){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"密码长度过短!");
        }

        //校验两次密码是否一致
        if (!userPassword.equals (checkPassword)){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"两次密码输入不一致!");
        }

        //新密码不可以旧密码一致
        User byId = this.getById (id);
        String oldUserPassword = byId.getUserPassword ();
        String md5UserPassword = DigestUtils.md5DigestAsHex ((SALT + userPassword).getBytes ());
        if (oldUserPassword.equals (md5UserPassword)){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"新密码不可以和旧密码一致");
        }
        //更新用户
        User user = new User ();
        user.setId (id);
        user.setUserName (userName);
         user.setUserAvatar (userAvatar);
        user.setUserProfile (userProfile);
        user.setGender (gender);
        user.setUserPassword (md5UserPassword);

        boolean result = this.updateById (user);


        ThrowUtils.throwIf (!result,ErrorCode.SYSTEM_ERROR,"操作数据库失败!");

        //更新登录态的信息
        request.getSession ().removeAttribute (USER_LOGIN_STATE);  //移除之后新加进去
        request.getSession ().setAttribute (USER_LOGIN_STATE,this.getById (id));

        return result;
    }

}



