package com.liu.yuojbackend.model.dto.questionsubmit;


import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好内存
 * @Date 2024-07-18 23:22
 * 判题信息对象
 */
@Data
public class JudgeInfo implements Serializable {


    private static final long serialVersionUID = 6103368208875849484L;

    private String message; //信息

    private Long time; //时间

    private Long memory; //内存
}
