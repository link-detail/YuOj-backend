package com.liu.yuojbackend.model.dto.questionsubmit;

import com.liu.yuojbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-07-21 20:52
 * 提交题目查询请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -3757317721532165989L;


    /**
     * 提交题目用户id
     */
    private Long userId;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 判题状态（0-待判题 1-判题中 2-成功 3-失败）
     */
    private Integer status;
}
