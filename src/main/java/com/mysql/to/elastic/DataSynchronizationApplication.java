package com.mysql.to.elastic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mysql.to.elastic.mapper")
public class DataSynchronizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSynchronizationApplication.class, args);
    }

}
