package com.liu.yuojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-19 21:00
 * 题目修改请求体
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    private static final long serialVersionUID = -6543627295901287254L;

    /**
     * id
     */
     private Long id;

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
