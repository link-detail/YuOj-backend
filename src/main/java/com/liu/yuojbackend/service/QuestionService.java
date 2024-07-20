package com.liu.yuojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.yuojbackend.model.dto.question.QuestionQueryRequest;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.vo.question.QuestionVO;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * question -- questionVO
     */
    QuestionVO getQuestionVO(Question question);


    /**
     *分页获取列表（封装类）
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 转换 question(list）--- questionVO（list）
     *
     * @param page
     */

    Page<QuestionVO> getQuestionVOPage(Page<Question> page);

    /**
     *分页获取当前用户创建的资源列表
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest, HttpServletRequest request);
}
