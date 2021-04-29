package com.airong.springcloud.service.consumer.service.impl;

import com.airong.springcloud.service.consumer.model.User;
import com.airong.springcloud.service.consumer.service.DemoService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;

/**
 * 目前微服务之前的访问是通过ribbon来处理的，如果需要feign处理请参考<br>
 * @see <a href="https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html">spring document</a>
 */
@Service
public class DemoServiceImpl implements DemoService {


    private final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.provider.application.name}")
    private String applicationName;

    @Value("${service.provider.application.protocol}")
    private String protocol;

    @Override
    @HystrixCommand(fallbackMethod = "getPortFallBack")
    public Integer getPort() {
        return restTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl(protocol + "://" + applicationName + "/demo/port").build().encode().toUri(), Integer.class).getBody();
    }

    public Integer getPortFallBack() {
        return -1;
    }

    @Override
    public String login(String role, String name) {
        UriComponents build = UriComponentsBuilder.fromHttpUrl(protocol + "://" + applicationName + "/demo/login").queryParam("name",name).queryParam("role",role).build();
        return restTemplate.exchange(build.encode().toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        }).getBody();
    }


    private <T> HttpEntity<T> createRequestEntity(T body) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        headers.setContentType(mediaType);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            headers.set(headerName, header);
        }
        return new HttpEntity<>(body, headers);
    }

    @Override
    public String info() {
        UriComponents build = UriComponentsBuilder.fromHttpUrl(protocol + "://" + applicationName + "/demo/info").build();
        return restTemplate.exchange(build.encode().toUri(), HttpMethod.GET, createRequestEntity(null), new ParameterizedTypeReference<String>() {
        }).getBody();
    }

    @Override
    public String saveUser(User user) {
        UriComponents build = UriComponentsBuilder.fromHttpUrl(protocol + "://" + applicationName + "/demo/user").build();
        return restTemplate.exchange(build.encode().toUri(), HttpMethod.POST, createRequestEntity(user), new ParameterizedTypeReference<String>() {
        }).getBody();
    }
}
