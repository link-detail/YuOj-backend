package com.liu.yuojbackend.person.test;

import com.liu.yuojbackend.model.dto.question.JudgeCase;

import java.util.ArrayList;

/**
 * @author 刘渠好
 * @since 2024-10-07 22:23
 */
public class Demo1 {
    public static void main(String[] args) {
        ArrayList<JudgeCase> cases = new ArrayList<> ();
        JudgeCase judgeCase1 = new JudgeCase ();
        judgeCase1.setInput ("1 2");
        judgeCase1.setOutput ("3 4");
        JudgeCase judgeCase2 = new JudgeCase ();
        judgeCase2.setInput ("1 2");
        judgeCase2.setOutput ("3 4");
        cases.add (judgeCase1);
        cases.add (judgeCase2);
        System.out.println (cases);


    }
}
