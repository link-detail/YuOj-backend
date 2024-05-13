package com.liu.yuojbackend.person.aop;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.message.ReusableMessage;
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
 * @Date 2024-05-13 22:32
 * 日志aop
 */
@Aspect  //是一个横切点
@Component
@Slf4j
public class LogInterceptor {


    @Around ("execution(* com.liu.yuojbackend.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        //定时
        StopWatch stopWatch = new StopWatch ();
        stopWatch.start ();
        //获取http请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        //唯一id
        String reqId= UUID.randomUUID ().toString ();
        //请求参数
        Object[] args = joinPoint.getArgs ();
        //将请求参数拼接起来,用，隔离
        String reqParam="["+StringUtils.join (args,",")+"]";

        log.info ("request start reqId：{},reqParam：{},reqPath：{},ip：{}",reqId,reqParam,
                request.getRequestURI (), request.getRemoteHost ());

        Object result = joinPoint.proceed ();

        //结束计时
        stopWatch.stop ();
        long timeMillis = stopWatch.getLastTaskTimeMillis ();

        log.info ("request end reqId：{},time：{}",reqId,timeMillis);

        return result;

    }
}
