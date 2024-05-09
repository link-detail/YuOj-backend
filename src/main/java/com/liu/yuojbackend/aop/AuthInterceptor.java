package com.liu.yuojbackend.aop;

import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.plugin2.message.BestJREAvailableMessage;
import sun.swing.StringUIClientPropertyKey;

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
        String mustRole = authCheck.mustRole ();
        //获取请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes ();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest ();

        //当前登录用户
        User loginUser = userService.getLoginUser (request);
        //必须有权限才可以通过
        if (StringUtils.isNotBlank (mustRole)){
            UserRoleEnum mustEnumByValue = UserRoleEnum.getEnumByValue (mustRole);
            if (mustEnumByValue==null){
                throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
            }
            String userRole = loginUser.getUserRole ();
            //如果封号直接拒绝
            if (UserRoleEnum.BAN.equals (mustEnumByValue)){
                throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
            }
            //必须是管理员权限才可以
            if (UserRoleEnum.ADMIN.equals (mustEnumByValue)){
                if (!mustRole.equals (userRole)){
                    throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
                }

            }
        }

        //通过校验，放行
        return joinPoint.proceed ();

    }
}
