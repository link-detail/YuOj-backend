package com.liu.yuojbackend.model.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 19:43
 * 题目信息枚举类
 */
public enum JudgeInfoMessageEnum {
    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    COMPILE_ERROR("编译错误", "Compile Error"),
    MEMORY_LIMIT_EXCEEDED("内存溢出", "Memory Out"),
    TIME_LIMIT_EXCEEDED("超时","Time Limit Exceeded"),
    PRESENTATION_ERROR("展示错误","Presentation Error"),
    WAITING("等待中","Waiting"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出","Output Limit Exceeded" ),
    DANGEROUS_OPERATION("危险操作","Dangerous Operation"),
    RUNTIME_ERROR("运行错误","Runtime Error"),
    SYSTEM_ERROR("系统错误","System Error");

    private final String text;
    private final String value;

    private JudgeInfoMessageEnum(String text,String value){
        this.text=text;
        this.value=value;
    }

    //根据value获取对应的枚举值
    public JudgeInfoMessageEnum getEnumByValue(String value){
        if (StringUtils.isEmpty (value)){
            return null;
        }
        for (JudgeInfoMessageEnum judgeInfoMessageEnum : JudgeInfoMessageEnum.values ()) {
            if (judgeInfoMessageEnum.getValue ().equals (value)){
                return judgeInfoMessageEnum;
            }
        }
        return null;
    }
    //获取全部的值
    public List<String> getAllValue(){
        return Arrays.stream (values ()).map (judgeInfoMessageEnum -> getValue ()).collect(Collectors.toList());
    }

    public String getText(){
        return this.text;
    }

    private String getValue(){
        return this.value;
    }
}
