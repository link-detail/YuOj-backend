package com.liu.yuojbackend.person.threadLoccal;

import com.liu.yuojbackend.model.entity.User;

/**
 * @Author 刘渠好
 * @Date 2024-07-17 20:30
 */
public class Demo1 {
    public static void main(String[] args) {
        User user = new User ();
        user.setUserAccount ("mia");
        user.setUserPassword ("123456789");
        //保存到线程变量中
        UserThreadLocal.setCurrentUser (user);

        //获取线程中的用户信息
        System.out.println (UserThreadLocal.getCurrentUser ().getUserAccount ());


    }
}
