package com.liu.yuojbackend.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.liu.yuojbackend.annotation.AuthCheck;
import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.DeleteRequest;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.model.dto.question.*;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.UserRoleEnum;
import com.liu.yuojbackend.model.vo.question.QuestionVO;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

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
     * 新增题目（管理员可以新增）
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request){
        //校验参数
        if (questionAddRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"参数不可以为空！");
        }
        Question question = new Question ();
        BeanUtils.copyProperties (questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags ();
        //处理json格式的数据
        if (CollUtil.isNotEmpty (tags)){
            question.setTags (GSON.toJson (tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase ();
        if (CollUtil.isNotEmpty (judgeCase)){
            question.setJudgeCase (GSON.toJson (judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig ();
        if (judgeConfig!=null){
            question.setJudgeConfig (GSON.toJson (judgeConfig));
        }
        //校验参数
        questionService.validQuestion (question, true);
        //获取当前用户
        User loginUser = userService.getLoginUser (request.getSession ());
        question.setUserId (loginUser.getId ());
        //保存
        boolean save = questionService.save (question);
        ThrowUtils.throwIf (!save, ErrorCode.OPERATION_ERROR);
        Long id = question.getId ();
        return ResultUtils.success (id);
    }
    /**
     * 删除题目（管理员可以删除）
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest){
        Long id = deleteRequest.getId ();
        //校验参数
        if (deleteRequest == null || id<=0){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"请求参数不可以为空!");
        }
        //判断是否有这个题目
        Question byId = questionService.getById (id);
        if (byId==null){
            throw new BusinessException (ErrorCode.NOT_FOUND_ERROR,"该题目不存在!");
        }
        //存在则删除
        boolean b = questionService.removeById (id);
        ThrowUtils.throwIf (!b, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success (b);
    }

    /**
     * 修改题目(只限于管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest){
        Long id = questionUpdateRequest.getId ();
        //校验参数
        if (questionUpdateRequest==null||id<=0){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"请求参数不可以为空!");
        }
        Question question = new Question ();
        BeanUtils.copyProperties (questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags ();
        //处理json格式的数据
        if (CollUtil.isNotEmpty (tags)){
            question.setTags (GSON.toJson (tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase ();
        if (CollUtil.isNotEmpty (judgeCase)){
            question.setJudgeCase (GSON.toJson (judgeCase));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig ();
        if (judgeConfig!=null){
            question.setJudgeConfig (GSON.toJson (judgeConfig));
        }
        //校验参数
        questionService.validQuestion (question, false);
        //只有存在才可以修改
        Question byId = questionService.getById (id);
        if (byId==null){
            throw new BusinessException (ErrorCode.NOT_FOUND_ERROR,"修改的题目不存在!");
        }
        //修改
        boolean b = questionService.updateById (question);
        ThrowUtils.throwIf (!b, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success (b);

    }

    /**
     * 查找题目
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id){
        //校验参数
        if (id<=0){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"题目id不能为空!");
        }
        //查找题目
        Question byId = questionService.getById (id);
        ThrowUtils.throwIf (byId==null, ErrorCode.NOT_FOUND_ERROR);
        //脱敏
        QuestionVO questionVO = questionService.getQuestionVO (byId);
        return ResultUtils.success (questionVO);
    }

    /**
     * 分页获取列表(封装类)
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest){
        //获取分页列表
        long pageSize = questionQueryRequest.getPageSize ();  //页面大小
        long current = questionQueryRequest.getCurrent ();  //当前页
        //限制爬虫
        ThrowUtils.throwIf (pageSize>20,ErrorCode.PARAMS_ERROR);
        Page<Question> page = questionService.page (new Page<> (current, pageSize), questionService.getQueryWrapper (questionQueryRequest));
        return ResultUtils.success (questionService.getQuestionVOPage (page));
    }

    /**
     * 分页获取当前用户的创建的资源列表
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,HttpServletRequest request){
        //校验参数
        if (questionQueryRequest==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        //获取分页列表
        long pageSize = questionQueryRequest.getPageSize ();  //页面大小
        long current = questionQueryRequest.getCurrent ();  //当前页
        //限制爬虫
        ThrowUtils.throwIf (pageSize>20,ErrorCode.PARAMS_ERROR);
        Page<Question> page = questionService.page (new Page<> (current, pageSize), questionService.getQueryWrapper (questionQueryRequest,request));
        return ResultUtils.success (questionService.getQuestionVOPage (page));
    }

    /**
     * 分页获取题目列表（仅管理员）
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest){
        //获取分页列表
        long pageSize = questionQueryRequest.getPageSize ();  //页面大小
        long current = questionQueryRequest.getCurrent ();  //当前页
        //限制爬虫
        ThrowUtils.throwIf (pageSize>20,ErrorCode.PARAMS_ERROR);
        Page<Question> page = questionService.page (new Page<> (current, pageSize), questionService.getQueryWrapper (questionQueryRequest));
        return ResultUtils.success (page);
    }

    /**
     * 编辑（用户）
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request){
        Long id = questionEditRequest.getId ();
        //校验参数
        if (questionEditRequest==null || id<=0){
            throw new BusinessException (ErrorCode.PARAMS_ERROR);
        }
        //校验参数值
        Question question = new Question ();
        BeanUtils.copyProperties (questionEditRequest,question);
        //json数据处理
        if (questionEditRequest.getTags ()!=null){
            question.setTags (GSON.toJson (questionEditRequest.getTags ()));
        }
        if (questionEditRequest.getJudgeCase ()!=null){
            question.setJudgeCase (GSON.toJson (questionEditRequest.getJudgeCase ()));
        }
        if (questionEditRequest.getJudgeConfig ()!=null){
            question.setJudgeCase (GSON.toJson (questionEditRequest.getJudgeConfig ()));
        }
        questionService.validQuestion (question,true);
        Question question1 = questionService.getById (id);
        //这个题目是否存在
        if (question1==null){
            throw new BusinessException (ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser (request.getSession ());
        boolean b = false;
        //只有自己或者是管理员可以编辑
        if (loginUser.getId ().equals (question1.getUserId ()) || userService.isAdmin (loginUser)){
            //编辑
            b = questionService.updateById (question);
        }
        ThrowUtils.throwIf (!b,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success (b);
    }


}

