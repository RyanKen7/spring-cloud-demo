package com.airong.springcloud.service.provider;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = {"com.airong"})
@EnableEurekaClient
public class ProviderApplication {


    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class,args);
    }

}
