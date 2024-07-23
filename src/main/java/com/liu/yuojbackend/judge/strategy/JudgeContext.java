package com.liu.yuojbackend.judge.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.util.concurrent.CycleDetectingLockFactory;
import com.liu.yuojbackend.judge.JudgeService;
import com.liu.yuojbackend.model.dto.question.JudgeCase;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-23 22:07
 * 上下文（用来陪判断题目对错）
 */
@Data
public class JudgeContext {

    //题目的输入（原题目中的输入）
    private List<String> inputList;

    //题目的输出（代码沙箱）
    private List<String> outputList;

    //代码沙箱返回答题的信息
    private JudgeInfo judgeInfo;

    //原题目输入输出用例
    private List<JudgeCase> judgeCases;

    //原题目
    private Question question;

    //提交题目
    private QuestionSubmit questionSubmit;

}
