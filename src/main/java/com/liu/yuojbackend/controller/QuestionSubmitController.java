package com.liu.yuojbackend.controller;

import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.PageRequest;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.QuestionSubmitService;
import com.liu.yuojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author 刘渠好
 * @Date 2024-07-20 22:21
 * 提交题目接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {


    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 提交题目
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request){
        Long questionId = questionSubmitAddRequest.getQuestionId ();
        //校验参数
        if (questionSubmitAddRequest == null || questionId<=0){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        //当前用户
        User loginUser = userService.getLoginUser (request);
        return ResultUtils.success (questionSubmitService.doQuestionSubmit(questionSubmitAddRequest,loginUser));

    }
}
