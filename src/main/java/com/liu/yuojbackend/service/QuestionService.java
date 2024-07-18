package com.liu.yuojbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.yuojbackend.model.entity.Question;

/**
* @author 刘渠好
* 针对表【question(题目表)】的数据库操作Service

*/
public interface QuestionService extends IService<Question> {
    /**
     * 校验参数
     * @param question
     */

    void validQuestion(Question question, boolean add);
}
