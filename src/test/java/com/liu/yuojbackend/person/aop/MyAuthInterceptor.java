package com.liu.yuojbackend.person.aop;

import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author 刘渠好
 * @since 2024-10-08 21:40
 *权限校验
 */
@Aspect  //切面
@Component
public class MyAuthInterceptor{

    @Resource
    private UserService userService;


    //使用该注解的地方都要经过这个权限校验
    @Around ("@annotation(authCheck)")
    public JoinPoint authCheck(JoinPoint joinPoint, AuthCheck authCheck){
        //判断需要的权限
        UserRoleEnum[] userRoleEnums = authCheck.mustRole ();
        //获取请求对象
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取当前登录用户
        User loginUser = userService.getLoginUser (request.getSession ());
        //1.用户未登录
        if (loginUser==null){
            throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR);
        }
        //2.用户权限禁止
        if (loginUser.getUserRole ().equals (UserRoleEnum.BAN)){
            throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
        }
        //3.是否是管理员权限
        if (Arrays.stream (userRoleEnums).noneMatch (userRoleEnum -> userRoleEnum==loginUser.getUserRole ())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint;
    }

}
