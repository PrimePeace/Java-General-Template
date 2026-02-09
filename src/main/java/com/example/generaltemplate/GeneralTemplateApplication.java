package com.example.generaltemplate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Been注解
 */
@MapperScan("com.example.generaltemplate.mapper")
public class GeneralTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneralTemplateApplication.class, args);
    }

}
