package com.liu.yuojbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.constant.CommonConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.mapper.QuestionMapper;
import com.liu.yuojbackend.model.dto.question.JudgeConfig;
import com.liu.yuojbackend.model.dto.question.QuestionQueryRequest;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.vo.question.QuestionVO;
import com.liu.yuojbackend.model.vo.user.UserVO;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.UserService;
import com.liu.yuojbackend.utils.SqlUtils;
import io.swagger.util.Json;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【question(题目表)】的数据库操作Service实现
* @createDate 2024-07-18 23:06:14
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private UserService userService;

    /**
     * 校验参数
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question==null){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"参数为空!");
        }
        String title = question.getTitle ();
        String content = question.getContent ();
        String tags = question.getTags ();
        String answer = question.getAnswer ();
        String judgeCase = question.getJudgeCase ();
        String judgeConfig = question.getJudgeConfig ();
        if (add){
            ThrowUtils.throwIf (StringUtils.isAnyBlank (tags, title, content), ErrorCode.PARAMS_ERROR);
        }
        //参数校验
        if (StringUtils.isNotBlank (title) && title.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"标题过长!");
        }
          if (StringUtils.isNotBlank (content) && content.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"内容过长!");
        }
          if (StringUtils.isNotBlank (answer) && answer.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"答案过长!");
        }
          if (StringUtils.isNotBlank (judgeCase) && judgeCase.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"判题用例过长!");
        }
          if (StringUtils.isNotBlank (judgeConfig) && judgeCase.length ()>88){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"判题配置过长!");
        }



    }

    /**
     * question -- questionVO
     *
     * @param question
     * @return
     */
    @Override
    public QuestionVO getQuestionVO(Question question) {
        QuestionVO questionVO = QuestionVO.objToVO (question);

        //关联用户信息
        Long userId = question.getUserId ();
        User user = null;
        if (userId!=null || userId>0){
            user=userService.getById (userId);
        }
        questionVO.setUserVO (userService.getUserVO (user));
        return questionVO;
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) { 
        //拼接条件
        QueryWrapper<Question> queryWrapper = new QueryWrapper<> ();
        if (questionQueryRequest==null){
            return null;
        }
        Long id = questionQueryRequest.getId ();
        String title = questionQueryRequest.getTitle ();
        String content = questionQueryRequest.getContent ();
        String answer = questionQueryRequest.getAnswer ();
        List<String> tags = questionQueryRequest.getTags ();
        Long userId = questionQueryRequest.getUserId ();
        String sortField = questionQueryRequest.getSortField (); //排序字段
        String sortOrder = questionQueryRequest.getSortOrder ();  //排序规则
        //拼接查询条件
        queryWrapper.eq (ObjectUtils.isNotEmpty (id),"id", id);
        queryWrapper.eq (ObjectUtils.isNotEmpty (userId),"userId", userId);
        queryWrapper.like (StringUtils.isNotBlank (title),"title", title);
        queryWrapper.like (StringUtils.isNotBlank (content),"content", content);
        queryWrapper.like (StringUtils.isNotBlank (answer),"answer", answer);
        for (String tag : tags) {
            queryWrapper.like ("tags","\""+tag+"\"");
        }
        queryWrapper.orderBy (SqlUtils.validSortField (sortField),sortOrder.equals (CommonConstant.SORT_ORDER_ASC),sortField);
        return queryWrapper;

    }

    /**
     * 类型转换
     * @param page
     * @return
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> page) {
        List<Question> questionList = page.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        //收集用户id
        Set<Long> idSet = questionList.stream ().map (Question::getUserId).collect (Collectors.toSet ());
        //每一个id对应一个用户
        Map<Long, List<User>> userIdUserListMap = userService.listByIds (idSet).stream ().collect (Collectors.groupingBy (User::getId));
        //填充信息
        List<QuestionVO> questionVOS = questionList.stream ().map (question -> {
            QuestionVO questionVO = new QuestionVO ();
            BeanUtils.copyProperties (question, questionVO);
            questionVO.setTags (JSONUtil.toList (question.getTags (), String.class));
            questionVO.setJudgeConfig (JSONUtil.toBean (question.getJudgeConfig (), JudgeConfig.class));
            //关联用户信息
            if (userIdUserListMap.containsKey (question.getUserId ())) {
                User user = userIdUserListMap.get (question.getUserId ()).get (0);
                UserVO userVO = userService.getUserVO (user);
                questionVO.setUserVO (userVO);
            }
            return questionVO;
        }).collect (Collectors.toList ());
        return questionVOPage.setRecords (questionVOS);

//        //收集信息(笨方法)
//        List<QuestionVO> questionVOList =new ArrayList<> ();
//        for (Question question : questionList) {
//            QuestionVO questionVO = new QuestionVO ();
//            BeanUtils.copyProperties (question,questionVO);
//            //jso数据
//            String tags = question.getTags ();
//            questionVO.setTags (JSONUtil.toList (tags,String.class));
//            String judgeConfig = question.getJudgeConfig ();
//            questionVO.setJudgeConfig (JSONUtil.toBean (judgeConfig, JudgeConfig.class));
//            //关联用户信息
//            Long userId = question.getUserId ();
//            User byId = userService.getById (userId);
//            UserVO userVO= null;
//            if (byId!=null){
//                userVO=userService.getUserVO (byId);
//            }
//            questionVO.setUserVO (userVO);
//            questionVOList.add (questionVO);
//
//        }
//        questionVOPage.setRecords (questionVOList);
//         1. 关联查询用户信息
//        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());//收集用户id
//        Map<Long, List<User>> userIdUserListMap = userService.listByIds (userIdSet).stream ()
//                .collect (Collectors.groupingBy (User::getId));
//        // 填充信息
//        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
//            QuestionVO questionVO = QuestionVO.objToVO (question);
//            Long userId = question.getUserId();
//            User user = null;
//            if (userIdUserListMap.containsKey(userId)) {
//                user = userIdUserListMap.get(userId).get(0);
//            }
//            questionVO.setUserVO(userService.getUserVO(user));
//            return questionVO;
//        }).collect(Collectors.toList());
//        questionVOPage.setRecords(questionVOList);
//        return questionVOPage;
    }

}




