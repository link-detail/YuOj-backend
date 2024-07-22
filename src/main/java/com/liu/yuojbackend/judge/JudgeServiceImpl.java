package com.liu.yuojbackend.judge;


import cn.hutool.json.JSONUtil;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.judge.codesandbox.CodeSandBox;
import com.liu.yuojbackend.judge.codesandbox.CodeSandBoxFactory;
import com.liu.yuojbackend.judge.codesandbox.CodeSandboxProxy;
import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import com.liu.yuojbackend.model.dto.question.JudgeCase;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.liu.yuojbackend.model.enums.QuestionSubmitStatusEnum;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 22:12
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Value ("${codesandbox.type}")
    private String type;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1.获取对应题目信息
        QuestionSubmit questionSubmit = questionSubmitService.getById (questionSubmitId);
        if (questionSubmit==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"提交信息不存在！");
        }
        //判断题目是否存在
        Question question = questionService.getById (questionSubmit.getQuestionId ());
        if (question==null){
            throw new BusinessException (ErrorCode.NOT_FOUND_ERROR,"题目不存在!");
        }
        //2.如果提交题目状态不是等待中，就不用重复执行 todo 这里多理解理解
        if (!questionSubmit.getStatus ().equals (QuestionSubmitStatusEnum.WAITING.getValue ())){
            throw new BusinessException (ErrorCode.OPERATION_ERROR,"该题目正在判题中!");
        }
        //3.更改题目的状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit ();
        questionSubmitUpdate.setId (questionSubmitId);
        questionSubmitUpdate.setStatus (QuestionSubmitStatusEnum.RUNNING.getValue ());
        boolean b1 = questionSubmitService.updateById (questionSubmitUpdate);
        if (!b1){
            throw new BusinessException (ErrorCode.OPERATION_ERROR);
        }
        //4.调用沙箱，获取执行结果
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance (type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy (codeSandBox);
        //获取题目的输入用例
        List<JudgeCase> list = JSONUtil.toList (question.getJudgeCase (), JudgeCase.class);
        List<String> inputList = list.stream ().map (JudgeCase::getInput).collect (Collectors.toList ());
        ExecuteCodeRequest build = ExecuteCodeRequest.builder ().
                code (questionSubmit.getCode ()).
                language (questionSubmit.getLanguage ()).
                inputList (inputList).build ();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode (build);
        List<String> ouputList = executeCodeResponse.getOuputList ();
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo ();
        //5.设置题目的判题状态和信息


        //6.修改数据库中的判题结果
        QuestionSubmit questionSubmitResult = new QuestionSubmit ();
        questionSubmitResult.setId (questionSubmitId);
        questionSubmitResult.setJudgeInfo (JSONUtil.toJsonStr (judgeInfo));
        questionSubmitResult.setStatus (QuestionSubmitStatusEnum.SUCCEED.getValue ());
        boolean b2 = questionSubmitService.updateById (questionSubmitResult);
        ThrowUtils.throwIf (!b2,ErrorCode.OPERATION_ERROR,"修改失败！");
        //获取最近数据返回
        QuestionSubmit byId = questionSubmitService.getById (questionSubmitId);
        return byId;
    }
}
