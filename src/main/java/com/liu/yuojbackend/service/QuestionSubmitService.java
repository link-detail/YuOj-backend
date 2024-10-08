package com.liu.yuojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.vo.questionsubmit.QuestionSubmitVO;

import javax.servlet.http.HttpSession;

/**
* @author Administrator
* @description 针对表【submit_question(题目提交表)】的数据库操作Service
* @createDate 2024-07-18 23:07:15
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 添加提交题目
     * @param questionSubmitAddRequest
     * @param loginUser
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 查询条件
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取questionVO返回类
     *
     * @param page
     * @param session
     * @return
     */

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> page, HttpSession session);

    /**
     * 获取返回类对象
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, HttpSession session);
}
