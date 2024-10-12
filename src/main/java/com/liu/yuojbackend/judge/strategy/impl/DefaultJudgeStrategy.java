package com.liu.yuojbackend.judge.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.liu.yuojbackend.judge.strategy.model.JudgeContext;
import com.liu.yuojbackend.judge.strategy.JudgeStrategy;
import com.liu.yuojbackend.model.dto.question.JudgeCase;
import com.liu.yuojbackend.model.dto.question.JudgeConfig;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 刘渠好
 * @Date 2024-07-23 22:13
 * 默认判断策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        //原题目信息跟代码沙箱执行之后返回的信息对比，来确定提交题目的答题信息
        List<String> outputList = judgeContext.getOutputList ();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo ();
        //获取答题信息
        Long time = judgeInfo.getTime ();  //时间
        Long memory = judgeInfo.getMemory (); //内存
        List<JudgeCase> judgeCases = judgeContext.getJudgeCases ();
        Question question = judgeContext.getQuestion ();
        //返回更新之后的题目提交信息
        JudgeInfo judgeResponse = new JudgeInfo ();
        judgeResponse.setMemory (memory);
        judgeResponse.setTime (time);
        //默认是通过
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;

        //原题目的输出用例
        List<String> outCollect = judgeCases.stream ().map (JudgeCase::getOutput).collect (Collectors.toList ());

        //判断输出数量是否一致
        if (outputList.size ()!=outCollect.size ()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeResponse.setMessage (judgeInfoMessageEnum.getText ());
            return judgeResponse;
        }
        //判断输出内容是否一致
        for (int i = 0; i < outCollect.size (); i++) {
            if (!outCollect.get (i).equals (outputList.get (i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeResponse.setMessage (judgeInfoMessageEnum.getText ());
                return judgeResponse;
            }
        }

        //之后来判断所需内存，时间是否合理
        String config = question.getJudgeConfig ();
        JudgeConfig judgeConfig = JSONUtil.toBean (config, JudgeConfig.class);
        Long needMemory = judgeConfig.getMemoryLimit (); //题目所需内存
        Long needTime = judgeConfig.getTimeLimit (); //题目所需时间
        if (memory>needMemory){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeResponse.setMessage (judgeInfoMessageEnum.getText ());
            return judgeResponse;
        }
        if (time>needTime){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeResponse.setMessage (judgeInfoMessageEnum.getText ());
            return judgeResponse;
        }
        judgeResponse.setMessage (judgeInfoMessageEnum.getText ());
        return judgeResponse;
    }
}
