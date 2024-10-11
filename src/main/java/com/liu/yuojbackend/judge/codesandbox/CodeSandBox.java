package com.liu.yuojbackend.judge.codesandbox;

import com.liu.yuojbackend.judge.model.ExecuteCodeRequest;
import com.liu.yuojbackend.judge.model.ExecuteCodeResponse;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 19:24
 * 代码沙箱接口（注意：代码沙箱只是负责编译，运行代码，并且返回运行结果，不参与判题）
 */
public interface CodeSandBox {
    /**
     * 执行代码接口
     * 提交到代码沙箱（输入用例，代码，语言）
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
