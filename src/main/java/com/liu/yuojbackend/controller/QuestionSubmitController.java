package com.liu.yuojbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.vo.questionsubmit.QuestionSubmitVO;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.QuestionSubmitService;
import com.liu.yuojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
        User loginUser = userService.getLoginUser (request.getSession ());
        return ResultUtils.success (questionSubmitService.doQuestionSubmit(questionSubmitAddRequest,loginUser));
    }

    /**
     * 分页获取题目提交列表(除了管理员之外，普通用户只能看到非答案，提交代码等公开信息)
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,HttpServletRequest request){
        //获取分页信息
        long current = questionSubmitQueryRequest.getCurrent ();
        long pageSize = questionSubmitQueryRequest.getPageSize ();
        //防止爬虫
        ThrowUtils.throwIf (pageSize>=10,ErrorCode.PARAMS_ERROR);
        //查询
        Page<QuestionSubmit> page = questionSubmitService.page (new Page<> (current, pageSize), questionSubmitService.getQueryWrapper (questionSubmitQueryRequest));
        return ResultUtils.success (questionSubmitService.getQuestionSubmitVOPage(page,request));


    }
}
