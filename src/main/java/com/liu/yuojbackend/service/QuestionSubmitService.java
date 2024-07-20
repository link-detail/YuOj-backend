package com.liu.yuojbackend.service;

import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.yuojbackend.model.entity.User;

/**
* @author Administrator
* @description 针对表【submit_question(题目提交表)】的数据库操作Service
* @createDate 2024-07-18 23:07:15
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 添加提交题目
     * @param questionSubmitAddRequest
     * @param loginUser
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
