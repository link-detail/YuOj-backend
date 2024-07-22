package com.liu.yuojbackend.judge.codesandbox;

import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 19:24
 * 代码沙箱接口
 */
public interface CodeSandBox {
    /**
     * 执行代码接口
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
