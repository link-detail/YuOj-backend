package com.liu.yuojbackend.aop;

import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
        String mustRole = authCheck.mustRole ();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue (mustRole);
        //获取请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes ();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest ();

        //当前用户的权限
        User loginUser = userService.getLoginUser (request);
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue (loginUser.getUserRole ());
        if (userRoleEnum!=null){
//            if (userRoleEnum!=mustRoleEnum){
//                throw  new BusinessException (ErrorCode.NO_AUTH_ERROR);
//            }
            if (userRoleEnum.compareTo (mustRoleEnum)<0){
                throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
            }

    }

        //通过校验，放行(之后就去执行带有这个注解的方法)
        return joinPoint.proceed ();

    }
}
