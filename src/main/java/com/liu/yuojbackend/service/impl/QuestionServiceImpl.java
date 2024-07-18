package com.liu.yuojbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.mapper.QuestionMapper;
import com.liu.yuojbackend.model.dto.question.JudgeConfig;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Administrator
* @description 针对表【question(题目表)】的数据库操作Service实现
* @createDate 2024-07-18 23:06:14
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    /**
     * 校验参数
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"参数为空!");
        }
        String title = question.getTitle ();
        String content = question.getContent ();
        String tags = question.getTags ();
        String answer = question.getAnswer ();
        String judgeCase = question.getJudgeCase ();
        String judgeConfig = question.getJudgeConfig ();
        if (add){
            ThrowUtils.throwIf (StringUtils.isAnyBlank (tags, title, content), ErrorCode.PARAMS_ERROR);
        }
        //参数校验
        if (StringUtils.isNotBlank (title) && title.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"标题过长!");
        }
          if (StringUtils.isNotBlank (content) && content.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"内容过长!");
        }
          if (StringUtils.isNotBlank (answer) && answer.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"答案过长!");
        }
          if (StringUtils.isNotBlank (judgeCase) && judgeCase.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"判题用例过长!");
        }
          if (StringUtils.isNotBlank (judgeConfig) && judgeCase.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"判题配置过长!");
        }



    }
}




