package com.liu.yuojbackend.model.dto.question;

import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-07-20 21:52
 * 编辑题目请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionEditRequest extends QuestionBaseRequest implements Serializable {

    private static final long serialVersionUID = -4572534986563039902L;

    /**
     * ID
     */
    private Long id;

}
