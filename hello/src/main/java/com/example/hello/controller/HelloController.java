package com.example.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/hello")
    public String test(@RequestParam( required = true) Long id) {
        String a = restTemplate.getForObject("http://user/user/detail?id="+id,String.class);
        System.out.println(a);
        return "hello"+a;
    }

}
