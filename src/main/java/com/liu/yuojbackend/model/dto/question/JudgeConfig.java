package com.liu.yuojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-07-18 23:17
 */
//判题配置对象
@Data
public class JudgeConfig implements Serializable {

    //实现序列化操作
    private static final long serialVersionUID = -3869872330476330959L;

    private Long timeLimit; //时间限制（ms）

    private Long memoryLimit; //内存限制（kb）

    private Long stackLimit; //堆栈限制（kb）




}
