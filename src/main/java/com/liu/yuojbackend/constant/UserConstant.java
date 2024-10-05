package com.liu.yuojbackend.constant;

/**
 * @Author 刘渠好
 * @Date 2024-05-08 21:06
 * 用户常数
 *
 * 小技巧:这里为什么选择一个接口类，是因为接口类下面参数都是private static final
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE ="user_login";

    /**
     * 默认密码 11111111
     */
    String DEFAULT_PASSWORD="11111111";

    /**
     * 权限 admin  user ban
     */

    String ADMIN_ROLE="admin";

    String BAN_ROLE="ban";

    String USER_ROLE="user";
}


