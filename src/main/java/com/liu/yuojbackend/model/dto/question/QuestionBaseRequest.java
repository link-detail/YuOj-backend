package com.liu.yuojbackend.model.dto.question;

import lombok.Data;

import java.util.List;

/**
 * @author 刘渠好
 * @since 2024-10-07 21:24
 * 题目基本请求
 */
@Data
public class QuestionBaseRequest {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签（json数组）
     */
    private List<String> tags;

    /**
     * 答案
     */
    private String answer;

    /**
     * 判题用例（json数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（json数组）
     */
    private JudgeConfig judgeConfig;
}
