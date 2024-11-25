package com.xvpi.cas.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
public class GenerateToken {
    public static String generateToken(String[] args) {
        // 生成一个安全的随机密钥
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // 用户信息，不应包含敏感信息
        String userId = "123";
        String username = "test";

        // 当前时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // token过期时间，例如1小时后
        long expMillis = nowMillis + 3600000;
        Date exp = new Date(expMillis);

        // 生成token
        String jws = Jwts.builder()
                .setSubject(userId)
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();

        return jws;
    }


}


