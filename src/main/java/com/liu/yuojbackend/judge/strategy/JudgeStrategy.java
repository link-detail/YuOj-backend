package com.liu.yuojbackend.judge.strategy;

import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;

/**
 * @Author 刘渠好
 * @Date 2024-07-23 22:12
 * 判题策略
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
