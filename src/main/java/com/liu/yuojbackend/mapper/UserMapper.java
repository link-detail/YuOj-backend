package com.liu.yuojbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.yuojbackend.model.entity.User;

/**
* @author Administrator
 * @createDate 2024-05-07 21:23:37
 */
//@Mapper  //将mapper注入到spring中 （还可以直接在主启动类上加mapperScan）
public interface UserMapper extends BaseMapper<User> {

}




