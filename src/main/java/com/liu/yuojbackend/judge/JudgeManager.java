package com.liu.yuojbackend.judge;

import com.liu.yuojbackend.judge.strategy.DefaultJudgeStrategy;
import com.liu.yuojbackend.judge.strategy.JavaLanguageJudgeStrategy;
import com.liu.yuojbackend.judge.strategy.JudgeContext;
import com.liu.yuojbackend.judge.strategy.JudgeStrategy;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @Author 刘渠好
 * @Date 2024-07-23 23:14
 * 判题管理(简化应用)
 */
@Service
public class JudgeManager{
    /**
     * 执行判题
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        //根据代码语言来选择策略模式
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit ();
        String language = questionSubmit.getLanguage ();
        //模式策略
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy ();
        if (language.equals ("java")){
            judgeStrategy=new JavaLanguageJudgeStrategy ();
            return judgeStrategy.doJudge (judgeContext);
        }
        return judgeStrategy.doJudge (judgeContext);

    }
}
