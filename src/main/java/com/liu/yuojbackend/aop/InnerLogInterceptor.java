package com.liu.yuojbackend.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Author 刘渠好
 * @Date 2024-05-13 23:22
 * 内部日志拦截器
 */
@Aspect
@Component
@Slf4j
public class InnerLogInterceptor {

//        @Pointcut(value = "execution(* com.liu.yuojbackend.controller.*.*(..))")
//        public void pcl() {
//        }
//
//        @Before(value = "pcl()")
//        public void before(JoinPoint point) {
//            String name = point.getSignature().getName();
//            System.out.println(name + "方法开始执行 ．．．");
//        }
//
//        @After(value = "pcl()")
//        public void after(JoinPoint point) {
//            String name = point.getSignature().getName();
//            System.out.println(name + "方法执行结束 ．．．");
//        }
//
//        @AfterReturning(value = "pcl()", returning = "result")
//        public void afterReturning(JoinPoint point, Object result) {
//            //获取连接点的方法名
//            String name = point.getSignature().getName();
//            System.out.println(name + "方法返回值为：" + result);
//        }
//
//
//        @Around("pcl()")
//        public Object around(ProceedingJoinPoint pjp) throws Throwable {
//            //返回响应数据
//            return pjp.proceed ();
//        }
    }

