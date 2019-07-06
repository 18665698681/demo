package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/test")
public class DemoController {
    @GetMapping(value = "/hello")
    public String top(@RequestParam(defaultValue = "2", required = false) Integer process) {
        return "hello word";
    }

}
