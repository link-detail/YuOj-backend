package com.liu.yuojbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.mapper.QuestionSubmitMapper;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.QuestionSubmitLanguageEnum;
import com.liu.yuojbackend.model.enums.QuestionSubmitStatusEnum;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.QuestionSubmitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Administrator
* @description 针对表【submit_question(题目提交表)】的数据库操作Service实现
* @createDate 2024-07-18 23:07:15
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;


    /*
    添加题目提交记录
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //判断编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage ();
        if (QuestionSubmitLanguageEnum.getEnumByValue (language)==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"编程语言错误!");

        }
        Long questionId = questionSubmitAddRequest.getQuestionId ();

        //判断题目是否存在
        Question byId = questionService.getById (questionId);
        if (byId==null){
            throw new BusinessException (ErrorCode.NO_AUTH_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit ();
        questionSubmit.setUserId (loginUser.getId ());
        questionSubmit.setCode (questionSubmitAddRequest.getCode ());
        questionSubmit.setQuestionId (questionSubmitAddRequest.getQuestionId ());
        questionSubmit.setLanguage (language);
        //设置初始状态
        questionSubmit.setJudgeInfo ("{}");
        questionSubmit.setStatus (QuestionSubmitStatusEnum.WAITING.getValue ());
        //保存
        boolean save = this.save (questionSubmit);
        ThrowUtils.throwIf (!save,ErrorCode.OPERATION_ERROR,"数据插入失败!");
        return questionSubmit.getId ();
    }
}




