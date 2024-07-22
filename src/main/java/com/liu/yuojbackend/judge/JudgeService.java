package com.liu.yuojbackend.judge;

import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.QuestionSubmit;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 22:11
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
