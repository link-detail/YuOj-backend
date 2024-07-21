package com.liu.yuojbackend.model.vo.questionsubmit;

import cn.hutool.json.JSONUtil;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.liu.yuojbackend.model.vo.question.QuestionVO;
import com.liu.yuojbackend.model.vo.user.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 刘渠好
 * @Date 2024-07-21 21:06
 * 题目提交返回类
 */
@Data
public class QuestionSubmitVO implements Serializable {

    private static final long serialVersionUID = 5493164911486885024L;

    /**
     * id
     */
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
    private JudgeInfo judgeInfo;

    /**
     * 用户信息
     */
    private UserVO userVO;

    /**
     * 题目信息
     */
    private QuestionVO questionVO;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    //questionSubmit--questionSubmitVO
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit){
        if (questionSubmit==null){
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO ();
        BeanUtils.copyProperties (questionSubmit,questionSubmitVO);
        //处理其他数据类型
        String judgeInfo1 = questionSubmit.getJudgeInfo ();
        questionSubmitVO.setJudgeInfo (JSONUtil.toBean (judgeInfo1,JudgeInfo.class));
        return questionSubmitVO;
    }

    //questionSubmitVo--questionSubmit
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO){
        QuestionSubmit questionSubmit = new QuestionSubmit ();
        if (questionSubmitVO==null){
            return null;
        }
        BeanUtils.copyProperties (questionSubmitVO,questionSubmit);
        //处理其他数据
        if (questionSubmitVO.getJudgeInfo ()!=null){
            questionSubmit.setJudgeInfo (JSONUtil.toJsonStr (questionSubmit.getJudgeInfo ()));
        }
        return questionSubmit;

    }
}
