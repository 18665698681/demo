package com.example.demo.controller.User;

import com.example.demo.model.User;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "demo2Controller")
@RequestMapping("/user")
public class Demo2Controller {
    @Autowired
    private DemoService demoService;

    @GetMapping(value = "/detail")
    public User top(@RequestParam( required = false) Long id) {
        return demoService.selectById(id);
    }

    @GetMapping(value = "/query/name")
    public User top(@RequestParam( required = false) String username) {
        return demoService.selectByUserName(username);
    }

}
