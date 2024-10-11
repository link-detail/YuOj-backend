package com.liu.yuojbackend.judge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 19:30
 * 执行代码请求参数(代码沙箱)
 */
@Data
@Builder //链式注解
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {

    //输入用例
    private List<String> inputList;

    //代码
    private String code;

    //语言
    private String language;



}
