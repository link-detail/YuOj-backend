package com.liu.yuojbackend.model.dto.question;

import lombok.Data;

 import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-07-18 23:20
 * 判题用例类
 *
 */
@Data
public class JudgeCase implements Serializable {


    private static final long serialVersionUID = -266816877479901822L;

    private String input;  //输入用例

    private String output; //输出用例
}
