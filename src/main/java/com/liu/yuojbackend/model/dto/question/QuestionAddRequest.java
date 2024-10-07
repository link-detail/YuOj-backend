package com.liu.yuojbackend.model.dto.question;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-07-18 23:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionAddRequest extends QuestionBaseRequest implements Serializable {

    private static final long serialVersionUID = 1898748361377614356L;
}
