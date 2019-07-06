package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//开启对EurekaServer的支持，即：作为Eureka服务端
@EnableEurekaServer
//开启作为Eureka Server的客户端的支持
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudEureckApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEureckApplication.class, args);
    }

}
