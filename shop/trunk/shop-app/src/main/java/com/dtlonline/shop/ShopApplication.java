package com.dtlonline.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yuanx
 * @date 2018/12/3
 */
@SpringBootApplication(scanBasePackages = {
        "io.alpha.app.**",
        "com.dtlonline.shop"})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.dtlonline.api.**")
@MapperScan({"com.**.shop.**.mapper"})
@EnableTransactionManagement
@EnableScheduling
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}