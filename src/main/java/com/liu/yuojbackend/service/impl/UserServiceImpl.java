package com.liu.yuojbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.constant.CommonConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.mapper.UserMapper;
import com.liu.yuojbackend.model.dto.user.UserQueryRequest;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.model.vo.user.LoginUserVO;
import com.liu.yuojbackend.model.vo.user.UserVO;
import com.liu.yuojbackend.service.UserService;
import com.liu.yuojbackend.utils.CopyUtil;
import com.liu.yuojbackend.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.liu.yuojbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 刘渠好
* @description  user(用户表)的数据库操作Service实现
* @createDate  2024-05-07 21:23:37
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    //盐值，混淆密码
    public static final String SALT ="liu";
    /**
     * 用户注册
     */
    @Override
    public long userResister(String userAccount, String userPassword, String checkPassword) {
        /**
         * 在查询数据库的时候，先把数据库之外的操作做了，之后再去查询数据库，避免浪费数据库资源
         */

        /**
         * 加锁：以确保在多线程环境中，同一时间只有一个线程能够执行这段代码块中的代码
         */
        //加锁(避免重复去注册)
        synchronized (userAccount.intern ()) {
            //1.账号不可以重复
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
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpSession session) {

        //1.判断用户是否存在
        //密码是明文的，需要加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex ((SALT + userPassword).getBytes ());
//常规查询
//          QueryWrapper<User> queryWrapper = new QueryWrapper<> ();
//          queryWrapper.eq ("userAccount", userAccount);
//                  queryWrapper.eq ("userPassword", md5DigestAsHex);
//                  User user = this.baseMapper.selectOne (queryWrapper);

        //lambda表达式查询
        User user = this.baseMapper.selectOne (
                Wrappers.lambdaQuery (User.class)
                .eq (User::getUserAccount, userAccount)
                .eq (User::getUserPassword, md5DigestAsHex)
        );

        //用户不存在
        if (user == null) {
            log.info ("user login failed,userAccount cannot match userPassword");
            throw new BusinessException (ErrorCode.NOT_FOUND_ERROR, "用户不存在或者账号密码错误!");
        }
        //2.判断该用户是否被加入黑名单
        if (user.getUserRole ().equals (UserRoleEnum.BAN) ){
            throw new BusinessException (ErrorCode.FORBIDDEN_ERROR, "该用户已经被加入黑名单,请合法使用账号!");
        }
        //3.记录用户的登录态
        session.setAttribute (USER_LOGIN_STATE, user);
         //4.返回信息(脱敏之后的数据)
        return  this.getLoginUserVO (user);

    }

    /**
     * 用户注销
     */
    @Override
    public boolean userLogout(HttpSession session) {
        //判断是都登陆
        if (session.getAttribute (USER_LOGIN_STATE)==null){
            throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR,"未登录!");
        }
        // 移除登录态
        session.removeAttribute (USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public User getLoginUser(HttpSession session) {
        //从session中获取用户信息
        User loginUser = (User) session.getAttribute (USER_LOGIN_STATE);

        // 先判断是否登录
        if (loginUser == null|| loginUser.getId () == null){
            throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询(追求性能的话，可以注释，直接走缓存)
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
        LoginUserVO loginUserVO = CopyUtil.copy (user, LoginUserVO.class);
        loginUserVO.setUserRole (user.getUserRole ().getValue ());
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
        UserVO vo = CopyUtil.copy (user, UserVO.class);
        vo.setUserRole (user.getUserRole ().getValue ());
        return vo;
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
        queryWrapper.orderBy (SqlUtils.validSortField (sortField),
                sortOrder.equals (CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;





    }

    /**
     *用户列表转为用户封装列表
     */
    @Override
    public List<UserVO> getUserVOList(List<User> list) {
//        if (CollUtil.isEmpty (list)){
//            return new ArrayList<> ();
//        }
//
//        return list.stream ().map (this::getUserVO).collect(Collectors.toList());
        return CopyUtil.copyList (list,UserVO.class);
    }

    /**
     * 判断是否是管理员
     * @param user
     * @return
     */
    @Override
    public boolean isAdmin(User user) {
        /**
         * 笨方法
         *   if (user==null){
         *             throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR);
         *         }
         *         if (UserRoleEnum.ADMIN.getValue ().equals (user.getUserRole ())){
         *             return true;
         *         }
         *         return false;
         */
        //下次就这么写
        return user!=null && UserRoleEnum.ADMIN.getValue ().equals (user.getUserRole ());

    }


}



