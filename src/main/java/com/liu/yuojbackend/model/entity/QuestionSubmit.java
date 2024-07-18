package com.liu.yuojbackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交表
 */
@TableName(value ="submit_question")
@Data
public class QuestionSubmit implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 提交题目用户id
     */
    private Long userId;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题状态（0-待判题 1-判题中 2-成功 3-失败）
     */
    private Integer status;

    /**
     * 判题信息（json对象）
     */
    private String judgeInfo;

    /**
     * 创建时间
     */
    private Date creatTime;

    /**
     * 删除时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}