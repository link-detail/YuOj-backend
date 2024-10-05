package com.liu.yuojbackend.aop;

import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
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
 * @Author 刘渠好
 * @Date 2024-05-09 22:41
 * 权限校验 AOP
 */

@Aspect
@Component //也是一个组件 被spring托管
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     */
    @Around ("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //获取需要的权限
        UserRoleEnum[] userRoleEnums = authCheck.mustRole ();

        //获取请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes ();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest ();

        //当前用户的权限
        User loginUser = userService.getLoginUser (request.getSession ());

        //用户是否登录
        if (ObjectUtils.isEmpty (loginUser)){
            throw new BusinessException (ErrorCode.NOT_LOGIN_ERROR);
        }
        //用户是否在黑名单内
        UserRoleEnum userRole = loginUser.getUserRole ();
        if (userRole.equals (UserRoleEnum.BAN)){
            throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
        }
        //是否具备权限
        if (Arrays.stream (userRoleEnums).noneMatch (userRoleEnum -> userRoleEnum==userRole)){
            throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
        }

        //通过校验，放行(之后就去执行带有这个注解的方法)
        return joinPoint.proceed ();

    }
}
