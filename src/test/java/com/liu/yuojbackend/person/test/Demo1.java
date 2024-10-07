package com.liu.yuojbackend.person.test;


import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * @author 刘渠好
 * @since 2024-10-07 22:23
 */
public class Demo1 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<> ();
        list.add (1);
        list.add (2);
        list.add (1);
        System.out.println (list);
        LinkedHashSet<Integer> set = new LinkedHashSet<> ();
        set.add (1);
        set.add (2);
        set.add (1);
        System.out.println (set);
    }
}
