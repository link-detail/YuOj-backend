package com.liu.yuojbackend.person.mytest;

import com.liu.yuojbackend.judge.JudgeService;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 刘渠好
 * @since 2024-10-12 21:43
 */
@SpringBootTest
public class Demo1 {

    @Resource
    private JudgeService judgeService;

    @Test
    void Demo1() {
        QuestionSubmit questionSubmit = judgeService.doJudge (1843667680341925890L);
        System.out.println (questionSubmit);
    }

}
