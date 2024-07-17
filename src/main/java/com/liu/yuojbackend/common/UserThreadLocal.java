package com.liu.yuojbackend.common;

import com.liu.yuojbackend.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author 刘渠好
 * @Date 2024-07-17 20:26
 * 使用ThreadLocal来存储用户信息
 */
@Component(value = "userThread")
@Slf4j
public class UserThreadLocal {

        // 定义一个ThreadLocal变量来存储用户信息
        private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

        public static void setCurrentUser(User user) {
            currentUser.set(user); // 设置当前线程的用户信息
            log.info ("Thread ID：{},设置用户信息：{}", Thread.currentThread ().getId (), user);

        }

        public static User getCurrentUser() {
            User user = currentUser.get ();// 获取当前线程的用户信息
            log.info ("Thread ID：{},获取用户信息：{}", Thread.currentThread ().getId (), user);
            return user;

        }

        public static void removeCurrentUser() {
            currentUser.remove(); // 清理当前线程的用户信息
        }
    }

