package com.liu.yuojbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 20:59
 * 删除用户请求类
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 5343600292905143818L;
    /**
     * 用户id
     */
    private Long id;


}
