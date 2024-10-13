package com.liu.yuojbackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.constant.CommonConstant;
import com.liu.yuojbackend.exception.BusinessException;
import com.liu.yuojbackend.exception.ThrowUtils;
import com.liu.yuojbackend.mapper.QuestionMapper;
import com.liu.yuojbackend.model.dto.question.QuestionQueryRequest;
import com.liu.yuojbackend.model.entity.Question;
import com.liu.yuojbackend.model.entity.User;
import com.liu.yuojbackend.model.vo.question.QuestionVO;
import com.liu.yuojbackend.service.QuestionService;
import com.liu.yuojbackend.service.UserService;
import com.liu.yuojbackend.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    //标题最大长度
    public static final int TITLE_MAX_LEN=80;
    //内容最大长度
    public static final int CONTENT_MAX_LEN=8192;

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
            //必须每一个都不是空
            ThrowUtils.throwIf (StringUtils.isAnyBlank (tags, title, content), ErrorCode.PARAMS_ERROR);
        }
        //参数校验
        if (StringUtils.isNotBlank (title) && title.length ()>TITLE_MAX_LEN){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"标题过长!");
        }
          if (StringUtils.isNotBlank (content) && content.length ()>CONTENT_MAX_LEN){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"内容过长!");
        }
          if (StringUtils.isNotBlank (answer) && answer.length ()>CONTENT_MAX_LEN){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"答案过长!");
        }
          if (StringUtils.isNotBlank (judgeCase) && judgeCase.length ()>CONTENT_MAX_LEN){
            throw new BusinessException (ErrorCode.PARAMS_ERROR,"判题用例过长!");
        }
          if (StringUtils.isNotBlank (judgeConfig) && judgeCase.length ()>CONTENT_MAX_LEN){
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
        if (userId!=null && userId>0){
            user=userService.getById (userId);
        }
        questionVO.setUserVO (userService.getUserVO (user));
        return questionVO;
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) { 
        //拼接条件  (基本写法:QueryWrapper<Question> queryWrapper= new QueryWrapper<> ();)
        QueryWrapper<Question> queryWrapper = Wrappers.query ();
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
        if (CollUtil.isNotEmpty (tags)){
            for (String tag : tags) {
//                queryWrapper.like ("tags","\""+tag+"\"");  //tags LIKE ? AND tags LIKE ?
            queryWrapper.like ("tags",tag);  //tags LIKE ? AND tags LIKE ?
            }
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
        List<Question> questionList = page.getRecords ();
        Page<QuestionVO> questionVOPage = new Page<> (page.getCurrent (), page.getSize (), page.getTotal ());
        if (CollectionUtils.isEmpty (questionList)) {
            return questionVOPage;
        }
//        收集用户信息(set集合元素里的元素是不可以重复的，用来收集用户id很适合)
        Set<Long> isSet = questionList.stream ().map (Question::getUserId).collect (Collectors.toSet ()); //收集用户id
//        以id来存储每一个用户对象(id:对象)  根据id进行分组
        Map<Long, List<User>> userMap = userService.listByIds (isSet).stream ().collect (Collectors.groupingBy (User::getId));
        //关联用户信息
        List<QuestionVO> questionVOS = questionList.stream ().map (question -> {
            QuestionVO questionVO = QuestionVO.objToVO (question);
            if (userMap.containsKey (question.getUserId ())) {
                //找到对应用户
                User user = userMap.get (question.getUserId ()).get (0);
                questionVO.setUserVO (userService.getUserVO (user));
            }

            return questionVO;
        }).collect (Collectors.toList ());
        questionVOPage.setRecords (questionVOS);

        return questionVOPage;


    }

}




