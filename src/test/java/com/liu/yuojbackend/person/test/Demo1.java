package com.liu.yuojbackend.person.test;

import cn.hutool.json.JSONUtil;
import com.liu.yuojbackend.model.dto.question.JudgeCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 刘渠好
 * @since 2024-10-07 22:23
 */
public class Demo1 {
    public static void main(String[] args) {
        JudgeCase judgeCase = new JudgeCase ();
        judgeCase.setInput ("1");
        judgeCase.setOutput ("2");
        judgeCase.setInput ("3");
        judgeCase.setOutput ("4");

    }
}
