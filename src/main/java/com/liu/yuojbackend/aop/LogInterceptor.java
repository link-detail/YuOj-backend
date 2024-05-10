package com.liu.yuojbackend.aop;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author 刘渠好
 * @Date 2024-05-10 22:30
 * 请求响应日志 AOP
 */
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    /**
     * 执行拦截
     * * ：代表返回任何类型
     * com.liu.yuojbackend.controller.*.*(..):com.liu.yuojbackend.controller包下所有类的所有方法的任意参数
     */
    @Around ("execution(* com.liu.yuojbackend.controller.*.*(..))")
    /**
     * 举例：登录
     * 此时这个连接点就是 execution (BaseResponse com.liu.yuojbackend.
     *      * controller.UserController.userLogin(UserLoginRequest,HttpServletRequest),org.apache,catalina,connector...)
     *      所以取请求参数的时候是从连接点取
     */
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        //计时
        StopWatch stopWatch = new StopWatch ();
        stopWatch.start ();
        //获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes ();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest ();

        //生成唯一id
        String reqId = UUID.randomUUID ().toString ();
        String requestURI = request.getRequestURI ();

        //获取请求参数(request 获取参数只适合从状态栏中输入的参数)
        Object[] args = joinPoint.getArgs ();
        String reqParam="["+ StringUtils.join (args,',')+"]";


        //输出请求日志
        log.info ("request start,id:{},path:{},ip:{},parmas:{}",reqId,requestURI,request.getRemoteHost (),reqParam);

        //执行原方法(到了这一步之后，就去执行拦截的方法了)
        Object result = joinPoint.proceed ();

        //输出响应日志
        stopWatch.stop ();
        long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis ();  //运行时间
        log.info ("request end,id:{},cost:{}",reqId, lastTaskTimeMillis);
        return result;


    }
}
