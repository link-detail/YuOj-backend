package com.liu.yuojbackend.person.judge;

import cn.hutool.core.date.DateTime;
import cn.hutool.extra.tokenizer.engine.jcseg.JcsegEngine;
import cn.hutool.json.JSONUtil;
import com.liu.yuojbackend.config.JsonConfig;
import com.liu.yuojbackend.judge.JudgeManager;
import com.liu.yuojbackend.judge.JudgeService;
import com.liu.yuojbackend.judge.JudgeServiceImpl;
import com.liu.yuojbackend.judge.codesandbox.CodeSandBox;
import com.liu.yuojbackend.judge.codesandbox.CodeSandBoxFactory;
import com.liu.yuojbackend.judge.codesandbox.CodeSandboxProxy;
import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.liu.yuojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import com.liu.yuojbackend.judge.strategy.JudgeContext;
import com.liu.yuojbackend.model.dto.question.JudgeCase;
import com.liu.yuojbackend.model.dto.question.JudgeConfig;
import com.liu.yuojbackend.model.dto.questionsubmit.JudgeInfo;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.liu.yuojbackend.model.enums.QuestionSubmitLanguageEnum;
import com.liu.yuojbackend.service.QuestionService;
import freemarker.core.JSONOutputFormat;
import io.swagger.util.Json;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.tomcat.Jar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 刘渠好
 * @Date 2024-07-22 21:23
 */
@SpringBootTest
class CodeSandTest {

    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeService judgeService;


    //获取配置文件中的内容
    @Value ("${codesandbox.type}")
    private String type;

    @Test
    void test01(){
         CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance (type);
        List<String> list = Arrays.asList ("1 2", "3 4");
         ExecuteCodeRequest build = ExecuteCodeRequest.builder ().code ("System.out.println (12);")
                .language (QuestionSubmitLanguageEnum.GO.getText ())
                .inputList (list).build ();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode (build);
        System.out.println (executeCodeResponse);

    }

    @Test
    void test02(){
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance (type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy (codeSandBox);
        List<String> list = Arrays.asList ("1 2", "3 4");
        ExecuteCodeRequest build = ExecuteCodeRequest.builder ().code ("System.out.println (12);")
                .language (QuestionSubmitLanguageEnum.GO.getText ())
                .inputList (list).build ();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode (build);
        System.out.println (executeCodeResponse);

    }

    @Test
    void test03(){
        QuestionSubmit questionSubmit = judgeService.doJudge (1815029258471759873L);
        System.out.println (questionSubmit);
    }
}
