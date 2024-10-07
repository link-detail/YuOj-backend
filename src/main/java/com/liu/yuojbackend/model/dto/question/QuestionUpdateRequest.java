package com.liu.yuojbackend.model.dto.question;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-07-19 21:00
 * 题目修改请求体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionUpdateRequest extends QuestionBaseRequest implements Serializable {

    private static final long serialVersionUID = -6543627295901287254L;

    /**
     * id
     */
     private Long id;


}
