package com.airong.springcloud.service.auth.utils;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 */
public class JwtUtil {

    private static final String JWT_KEY = "test";
    /**
     * 生成JWT
     * @param body /
     * @param role /
     * @return /
     */
    public static String getJWT(String body, String role) {
        long expires_in = 1000 * 60 * 60 * 24L; //一天
        long time = System.currentTimeMillis();
        time = time + expires_in;
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString()) //设置唯一ID
                .setSubject(body) //设置内容，这里用JSON包含帐号信息
                .setIssuedAt(new Date()) //签发时间
                .setExpiration(new Date(time)) //过期时间
                .claim("roles", role) //设置角色
                .signWith(SignatureAlgorithm.HS256, generalKey()) //设置签名 使用HS256算法，并设置密钥
                ;
        return builder.compact();
    }
    /**
     * 解析JWT
     * @param jwt /
     * @return /
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(jwt).getBody();
    }

    /**
     * 验证JWT
     * @param jwt /
     * @return /
     */
    public static boolean valid(String jwt) {
        try {
            Claims body = Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(jwt).getBody();
            if(body != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return /
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}
