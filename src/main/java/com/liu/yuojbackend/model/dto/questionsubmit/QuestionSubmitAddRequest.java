package com.liu.yuojbackend.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-07-20 22:23
 * 提交题目请求类
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

private static final long serialVersionUID = -432587407166235217L;
    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

}
