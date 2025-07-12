package com.jason.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Jason
 * @version 1.0
 */
@SpringBootApplication
// 扫描 MyBatis 的 Mapper 接口所在的包
@MapperScan("com.jason.community.mvc.mapper")
public class ZhuKeLaiMain {

    public static void main(String[] args) {
        SpringApplication.run(ZhuKeLaiMain.class, args);
    }
}
