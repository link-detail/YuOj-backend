package com.liu.yuojbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
/**
 * mapperScan作用
 * 可以告诉 Spring 在指定的包路径下查找 Mapper 接口，
 * 并将其注册为 Spring Bean，使得这些 Mapper 接口可以被注入到其他组件中使用
 */
@MapperScan("com.liu.yuojbackend.mapper")
public class YuOjBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run (YuOjBackendApplication.class, args);
    }

}
