package com.airong.springcloud.service.consumer.service;

import com.airong.springcloud.service.consumer.model.User;
import org.springframework.web.bind.annotation.*;

public interface DemoService {

    Integer getPort();

    String login(@RequestParam("role") String role, @RequestParam("name") String name);

    String info();

    String saveUser(User user);
}
