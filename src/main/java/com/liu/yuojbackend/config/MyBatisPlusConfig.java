package com.liu.yuojbackend.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 刘渠好
 * @Date 2024-05-16 23:05
 * MyBatis Plus 配置  (有了这个，分页才会生效)
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor ();
        //分页插件
        mybatisPlusInterceptor.addInnerInterceptor (new PaginationInnerInterceptor (DbType.MYSQL));
        return mybatisPlusInterceptor;
    }
}
