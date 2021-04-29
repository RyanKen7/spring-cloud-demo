package com.airong.springcloud.service.gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        /*
         * 如果需要新增路由则是
         * builders.routes()
         *         .route("spring-cloud-service-consumer",r -> r.path("/spring-cloud-service-consumer/**").filters(e -> e.stripPrefix(1)).uri("lb://SPRING-CLOUD-SERVICE-CONSUMER"))
         *         .route("spring-cloud-service-provider",r -> r.path("/spring-cloud-service-provider/**).filters(e -> e.stripPrefix(1)).uri("lb://SPRING-ClOUD-SERVICE-PROVIDER"))
         *         .route("route-id",r-> r.path("API-PATH/**").uri("lb://APPLICATION-NAME")).build();
         *         filter方法中的stripPrefix作用是去掉一层请求路径 如果请求路径是/a/b/c 那么经过网关转发的请求路径就是/b/c
         */
        return builder.routes().route("spring-cloud-service-consumer",r -> r.path("/spring-cloud-service-consumer/**").filters(e -> e.stripPrefix(1)).uri("lb://SPRING-CLOUD-SERVICE-CONSUMER")).build();

    }

}
