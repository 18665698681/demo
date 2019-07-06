package com.example.firstspringbootproject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class Hellocontroller {
    @RequestMapping("hello")
    String hello() {
        return "Hello World!!!!!!!!!!!!!!!!!";
    }
}

