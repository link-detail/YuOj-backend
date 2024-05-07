package com.liu.yuojbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.liu.yuojbackend.mapper")
public class YuOjBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run (YuOjBackendApplication.class, args);
    }

}
