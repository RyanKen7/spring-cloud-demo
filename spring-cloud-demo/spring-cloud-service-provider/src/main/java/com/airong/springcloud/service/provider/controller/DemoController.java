package com.airong.springcloud.service.provider.controller;

import com.airong.springcloud.service.auth.utils.JwtUtil;
import com.airong.springcloud.service.provider.model.User;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/port")
    public Integer port() {
        return port;
    }


    @GetMapping("/login")
    public String login(@RequestParam("name") String name,@RequestParam("role") String role) {
        return JwtUtil.getJWT(name,role);
    }

    @GetMapping("/info")
    public String info(HttpServletRequest request) {
        System.out.println("i got it!");
        String header = request.getHeader("jwt-token");
        Claims claims = JwtUtil.parseJWT(header);
        String subject = claims.getSubject();
        Object roles = claims.get("roles");
        return "Hello!" + subject + ",Your role is " + roles;
    }

    @PostMapping("/user")
    public String saveUser(@RequestBody User user) {
        return String.format("User %s saved",user.getUsername());
    }

}
