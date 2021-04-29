package com.airong.springcloud.service.gateway.filter;

import com.airong.springcloud.service.gateway.Response;
import com.airong.springcloud.service.auth.utils.JwtUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class AuthFilterConfiguration {

    private static final String TOKEN_HEADER = "jwt-token";

    @Value("${filter.while-list}")
    private List<String> whiteList;

    @Bean
    @Order
    public GlobalFilter globalFilter() {
        return new GlobalFilter() {
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                try {
                    ServerHttpRequest request = exchange.getRequest();
                    ServerHttpResponse response = exchange.getResponse();

                    String path = request.getURI().getPath();
                    if (whiteList.stream().anyMatch(path::endsWith)) {
                        return chain.filter(exchange);
                    }

                    HttpHeaders headers = request.getHeaders();
                    if (!headers.containsKey(TOKEN_HEADER)) {
                        return error(response, new Gson().toJson(Response.failed("1001", "jwt-token is required")));
                    }

                    List<String> tokens = headers.get(TOKEN_HEADER);
                    if (CollectionUtils.isEmpty(tokens)) {
                        return error(response, new Gson().toJson(Response.failed("1001", "jwt-token is required")));
                    }

                    String token = tokens.get(0);
                    if (!JwtUtil.valid(token)) {
                        return error(response, new Gson().toJson(Response.failed("1002", "token invalid.")));
                    }
                    return chain.filter(exchange);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return error(exchange.getResponse(), new Gson().toJson(Response.failed("1003", "interval server error.")));
                }
            }

            private Mono<Void> error(ServerHttpResponse response, String json) {
                //返回错误
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(buffer));
            }
        };
    }

}
