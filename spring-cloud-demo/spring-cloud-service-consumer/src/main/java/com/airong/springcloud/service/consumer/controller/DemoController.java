package com.airong.springcloud.service.consumer.controller;

import com.airong.springcloud.service.consumer.model.User;
import com.airong.springcloud.service.consumer.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/demo")
@RestController
public class DemoController {


    @Autowired
    private DemoService demoService;

    @GetMapping("/port")
    public Integer port() {
        return demoService.getPort();
    }


    @GetMapping("/login")
    public String login(@RequestParam("name") String name, @RequestParam("role") String role) {
        return demoService.login(role,name);
    }

    @GetMapping("/info")
    public String info(HttpServletRequest request) {
        return demoService.info();
    }

    @PostMapping(value = "/user")
    public String saveUser(@RequestBody User user) {
        return demoService.saveUser(user);
    }


}
