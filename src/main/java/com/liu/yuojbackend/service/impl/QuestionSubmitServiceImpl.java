package com.liu.yuojbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.constant.CommonConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.mapper.QuestionSubmitMapper;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.liu.yuojbackend.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.QuestionSubmit;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.enums.QuestionSubmitLanguageEnum;
import com.liu.yuojbackend.model.enums.QuestionSubmitStatusEnum;
import com.liu.yuojbackend.model.vo.questionsubmit.QuestionSubmitVO;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.QuestionSubmitService;
import com.liu.yuojbackend.service.UserService;
import com.liu.yuojbackend.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【submit_question(题目提交表)】的数据库操作Service实现
* @createDate 2024-07-18 23:07:15
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;


    /*
    添加题目提交记录
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //判断编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage ();
        if (QuestionSubmitLanguageEnum.getEnumByValue (language) == null) {
            throw new BusinessException (ErrorCode.PARAMS_ERROR, "编程语言错误!");

        }
        Long questionId = questionSubmitAddRequest.getQuestionId ();

        //判断题目是否存在
        Question byId = questionService.getById (questionId);
        if (byId == null) {
            throw new BusinessException (ErrorCode.NOT_FOUND_ERROR);
        }
        //设置提交题目信息
        QuestionSubmit questionSubmit = new QuestionSubmit ();
        questionSubmit.setUserId (loginUser.getId ());
        questionSubmit.setCode (questionSubmitAddRequest.getCode ());
        questionSubmit.setQuestionId (questionSubmitAddRequest.getQuestionId ());
        questionSubmit.setLanguage (language);
        //设置初始状态
        questionSubmit.setJudgeInfo ("{}");
        questionSubmit.setStatus (QuestionSubmitStatusEnum.WAITING.getValue ());
        //保存
        boolean save = this.save (questionSubmit);
        ThrowUtils.throwIf (!save, ErrorCode.OPERATION_ERROR, "数据插入失败!");
        return questionSubmit.getId ();
    }

    /*8
    查询条件wrapper
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<> ();
        if (questionSubmitQueryRequest == null) {
            return questionSubmitQueryWrapper;
        }
        Long userId = questionSubmitQueryRequest.getUserId ();
        Long questionId = questionSubmitQueryRequest.getQuestionId ();
        String language = questionSubmitQueryRequest.getLanguage ();
        Integer status = questionSubmitQueryRequest.getStatus ();
        String sortField = questionSubmitQueryRequest.getSortField ();
        String sortOrder = questionSubmitQueryRequest.getSortOrder ();
        //拼接条件
        questionSubmitQueryWrapper.eq (ObjectUtils.isNotEmpty (userId), "userId", userId);
        questionSubmitQueryWrapper.eq (ObjectUtils.isNotEmpty (questionId), "questionId", questionId);
        questionSubmitQueryWrapper.eq (StringUtils.isNotBlank (language), "language", language);
        //只有指定的等待条件才能进行查询
        questionSubmitQueryWrapper.eq (QuestionSubmitStatusEnum.getEnumByValue (status) != null, "status", status);
        //排序
        questionSubmitQueryWrapper.orderBy (
                SqlUtils.validSortField (sortField),
                sortOrder.equals (CommonConstant.SORT_ORDER_ASC),
                sortField);
        return questionSubmitQueryWrapper;


    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, HttpSession session) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo (questionSubmit);

        User loginUser = userService.getLoginUser (session);

        //只有管理员或者是自己可以查看提交代码信息
        if (!loginUser.getId ().equals (questionSubmit.getUserId ()) && !userService.isAdmin (loginUser)) {
            questionSubmitVO.setCode (null);
        }
        return questionSubmitVO;
    }

    /**
     * 获取提交题目返回类
     *
     * @param page
     * @param session
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> page, HttpSession session) {
//        //获取登录用户
//        User loginUser = userService.getLoginUser (request);
//        List<QuestionSubmit> questionSubmits = page.getRecords ();
//        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<> (page.getCurrent (), page.getSize (), page.getTotal ());
//        if (CollectionUtils.isEmpty (questionSubmits)) {
//            return questionSubmitVOPage;
//        }
//        //先处理对应用户信息
//        Set<Long> userIdSet = questionSubmits.stream ().map (QuestionSubmit::getUserId).collect (Collectors.toSet ());
//        //处理题目信息
//        Set<Long> questionIdSet = questionSubmits.stream ().map (QuestionSubmit::getQuestionId).collect (Collectors.toSet ());
//        //每一个id对应每一个用户信息
//        Map<Long, List<User>> userMap = userService.listByIds (userIdSet).stream ().collect (Collectors.groupingBy (User::getId));
//        //每一个id对应一个题目信息
//        Map<Long, List<Question>> questionMap = questionService.listByIds (questionIdSet).stream ().collect (Collectors.groupingBy (Question::getId));
//        //关联用户信息
//        List<QuestionSubmitVO> collect = questionSubmits.stream ().map (questionSubmit -> {
//            //处理用户信息
//            QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo (questionSubmit);
//            Long userId = questionSubmit.getUserId ();
//            if (userMap.containsKey (userId)) {
//                User user = userMap.get (userId).get (0);
//                questionSubmitVO.setUserVO (userService.getUserVO (user));
//            }
//            //处理题目信息
//            Long questionId = questionSubmit.getQuestionId ();
//            if (questionMap.containsKey (questionId)) {
//                Question question = questionMap.get (questionId).get (0);
//                questionSubmitVO.setQuestionVO (QuestionVO.objToVO (question));
//            }
//            //只有自己或者是管理员才可以看见自己的提交的代码
//            if (!questionSubmit.getUserId ().equals (loginUser.getId ()) && !userService.isAdmin (loginUser)) {
//                questionSubmitVO.setCode (null);
//            }
//            return questionSubmitVO;
//        }).collect (Collectors.toList ());
//        questionSubmitVOPage.setRecords (collect);
//        return questionSubmitVOPage;
        List<QuestionSubmit> questionSubmitList = page.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(page.getCurrent(),
                page.getSize(), page.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }

        //
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, session))
                .collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}







