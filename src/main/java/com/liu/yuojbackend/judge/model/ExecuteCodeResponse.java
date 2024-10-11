package com.liu.yuojbackend.judge.model;

import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 19:26
 * 执行代码返回类
 */
@Data
@Builder  //链式赋值
@AllArgsConstructor //有参构造函数
@NoArgsConstructor  //无参构造函数
public class ExecuteCodeResponse {

    //输出用例
    private List<String> ouputList;

    //执行信息(其他信息 跟judgeInfo中的不一样)
    private String message;

    //执行状态
    private Integer status;

    //判题信息
    private JudgeInfo judgeInfo;
}
