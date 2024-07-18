package com.liu.yuojbackend.person.question;

import com.liu.yuojbackend.model.entity.User;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 刘渠好
 * @Date 2024-07-19 0:17
 */
public class Demo1 {
    public static void main(String[] args) {
        Map<Long, List<User>> listMap = new HashMap<> ();
        List<User> users = new ArrayList<> ();
        User user = new User ();
        user.setId (1L);
        user.setUserName ("name");
        users.add (user);
        listMap.put (0L,users);

    }
}
