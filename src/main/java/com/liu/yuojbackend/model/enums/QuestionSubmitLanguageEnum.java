package com.liu.yuojbackend.model.enums;


import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 刘渠好
 * @Date 2024-07-20 22:46
 * 编程语言枚举类
 */
public enum QuestionSubmitLanguageEnum {
    JAVA("java","java"),
    GO("go","go"),
    PYTHON("python","python")
    ;

    private final String text;
    private final String value;

    private QuestionSubmitLanguageEnum(String text,String value){
        this.text=text;
        this.value=value;
    }
    //根据value值获取枚举对象
    public static QuestionSubmitLanguageEnum getEnumByValue(String value){
        if (ObjectUtils.isEmpty (value)){
            return null;
        }
        QuestionSubmitLanguageEnum[] values = QuestionSubmitLanguageEnum.values ();
        for (QuestionSubmitLanguageEnum questionSubmitLanguageEnum : values) {
            if (questionSubmitLanguageEnum.value.equals (value)){
                return questionSubmitLanguageEnum;
            }
        }
        return null;
    }
    //获取全部value值
    public static List<String> getValues(){
        return Arrays.stream(values ()).map (item -> item.value)
                .collect(Collectors.toList());
    }

    public String getText(){
        return text;
    }
    public String getValue(){
        return value;
    }


}
