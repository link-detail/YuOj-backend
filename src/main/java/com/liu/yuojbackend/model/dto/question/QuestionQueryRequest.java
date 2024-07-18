package com.liu.yuojbackend.model.dto.question;

import com.liu.yuojbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-19 22:30
 * 条件查询请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionQueryRequest extends PageRequest implements Serializable {


    private static final long serialVersionUID = -2890215207263896808L;

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
     * 题目答案
     */
    private String answer;

    /**
     * 标签（json数组）
     */
    private List<String> tags;

    /**
     * 创建用户id
     */
    private Long userId;

}
