package com.liu.yuojbackend.model.vo.question;

import cn.hutool.json.JSONUtil;
import com.liu.yuojbackend.model.dto.question.JudgeConfig;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.vo.user.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-19 21:19
 * 题目返回类
 */
@Data
public class QuestionVO implements Serializable {

private static final long serialVersionUID = 5518477179523221389L;

    /**
     * id
     */
     private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签（json数组）
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptNum;


    /**
     * 判题配置（json数组）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除时间
     */
    private Date updateTime;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    /**
     * 类型转换
     */
    public static QuestionVO objToVO(Question question){
        if (question==null){
            return null;
        }
        QuestionVO questionVO = new QuestionVO ();
        BeanUtils.copyProperties (question,questionVO);
        //json数据传输
        List<String> tagList = JSONUtil.toList (question.getTags (), String.class); //json字符串转为列表
        questionVO.setTags (tagList);
        JudgeConfig bean = JSONUtil.toBean (question.getJudgeConfig (), JudgeConfig.class);
        questionVO.setJudgeConfig (bean);
        return questionVO;
    }

    public static Question voToObj(QuestionVO questionVO){
        if (questionVO == null){
            return null;
        }
        Question question = new Question ();
        BeanUtils.copyProperties (questionVO,question);
        //json数据传输
        List<String> tags1 = questionVO.getTags ();
         if (tags1!=null){
            question.setTags (JSONUtil.toJsonStr (tags1));
        }
        JudgeConfig judgeConfig1 = questionVO.getJudgeConfig ();
        if (judgeConfig1!=null){
            question.setJudgeConfig (JSONUtil.toJsonStr (judgeConfig1));
        }
        return question;
    }

}
