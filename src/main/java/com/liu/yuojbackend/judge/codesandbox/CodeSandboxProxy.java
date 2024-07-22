package com.liu.yuojbackend.judge.codesandbox;

import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 21:43
 * 沙箱代理类
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;

    public CodeSandboxProxy(CodeSandBox codeSandBox){
        this.codeSandBox=codeSandBox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info ("代码沙箱请求信息：{}",executeCodeRequest.toString ());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode (executeCodeRequest);
        log.info ("代码沙箱响应信息：{}",executeCodeResponse.toString ());
        return executeCodeResponse;
    }
}
