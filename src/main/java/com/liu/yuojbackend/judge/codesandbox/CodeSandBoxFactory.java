package com.liu.yuojbackend.judge.codesandbox;

import com.liu.yuojbackend.judge.codesandbox.impl.ExampleCodeSandBox;
import com.liu.yuojbackend.judge.codesandbox.impl.RemoteCodeSandBox;
import com.liu.yuojbackend.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 20:22
 * 代码沙箱工厂
 */
public class CodeSandBoxFactory {
    /**
     * 创建代码沙箱市例 type(沙箱类型）
     */
    public static CodeSandBox newInstance(String type){
        switch (type){
            case "example":
                return new ExampleCodeSandBox ();
             case "remote":
                return new RemoteCodeSandBox ();
             case "third":
                return new ThirdPartyCodeSandBox ();
             default:
                return new ExampleCodeSandBox ();
        }
    }
}
