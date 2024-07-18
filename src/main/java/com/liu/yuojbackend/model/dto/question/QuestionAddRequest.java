package com.liu.yuojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-18 23:38
 */
@Data
public class QuestionAddRequest implements Serializable {

    private static final long serialVersionUID = 1898748361377614356L;
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
