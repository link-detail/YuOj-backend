package com.liu.yuojbackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.google.gson.Gson;
import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.constant.UserConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.model.dto.question.JudgeCase;
import com.liu.yuojbackend.model.dto.question.JudgeConfig;
import com.liu.yuojbackend.model.dto.question.QuestionAddRequest;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @Author 刘渠好
 * @Date 2024-07-18 23:35
 */
@RestController
@Slf4j
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson ();

    //region crud(增加 删除 修改题目只允许管理员进行操作)

    /**
     * 新增题目)
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        //检验参数
        if (questionAddRequest == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "参数不可以为空!");
        }
        Question question = new Question ();
        BeanUtil.copyProperties (questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags ();
        if(tags != null){
            question.setTags (GSON.toJson (tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase ();
        if (judgeCase!=null){
            question.setJudgeCase (GSON.toJson (judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig ();
        if(judgeCase!=null){
            question.setJudgeConfig (GSON.toJson (judgeConfig));
        }
        //校验参数
        questionService.validQuestion(question,true);
        //获取当前对象
        User loginUser = userService.getLoginUser (request);
        question.setUserId (loginUser.getId ());
//        question.setFavourNum (0);
//        question.setThumbNum (0);
        boolean save = questionService.save (question);
        ThrowUtils.throwIf (!save,ErrorCode.OPERATION_ERROR);
        Long id = question.getId ();
        return ResultUtils.success (id);
    }
}

