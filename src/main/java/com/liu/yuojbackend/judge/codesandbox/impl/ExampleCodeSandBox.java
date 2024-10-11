package com.liu.yuojbackend.judge.codesandbox.impl;

import com.liu.yuojbackend.judge.codesandbox.CodeSandBox;
import com.liu.yuojbackend.judge.model.ExecuteCodeRequest;
import com.liu.yuojbackend.judge.model.ExecuteCodeResponse;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.enums.JudgeInfoMessageEnum;
import com.liu.yuojbackend.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 19:35
 * 测试代码沙箱（为了跑通流程）
 */
@Slf4j
public class ExampleCodeSandBox implements CodeSandBox {
    /**
     * 沙箱执行代码
     * @param executeCodeRequest  执行代码请求参数
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse ();
        List<String> inputList = executeCodeRequest.getInputList ();
        executeCodeResponse.setOuputList (inputList);
        executeCodeResponse.setMessage ("测试执行成功！");
        executeCodeResponse.setStatus (QuestionSubmitStatusEnum.SUCCEED.getValue ());
        JudgeInfo judgeInfo = new JudgeInfo ();
        judgeInfo.setMessage (JudgeInfoMessageEnum.ACCEPTED.getText ());
        judgeInfo.setTime (100L);
        judgeInfo.setMemory (100L);
        executeCodeResponse.setJudgeInfo (judgeInfo);
        return executeCodeResponse;
    }
}
