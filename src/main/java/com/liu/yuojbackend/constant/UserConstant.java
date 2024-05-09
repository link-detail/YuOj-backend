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
     * 加密盐
     */
    String SALT="liu";

    /**
     * 登陆用户态
     */
    String USER_LOGIN_STATE ="user_login";

    /**
     * 默认密码 11111111
     */
    String DEFAULT_PASSWORD="11111111";
}
