package org.example.web_homework_server;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class WebHomeworkServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testGenJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("name", "tom");
        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "zero")//签名算法  密钥
                .setClaims(claims)// 载荷
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000 * 24)) //有效期24小时;
                .compact();

        System.out.println(jwt);
    }

    @Test
    public void testParseJwt() {
        Claims claims = Jwts.parser()
                .setSigningKey("zero")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidG9tIiwiaWQiOjEsImV4cCI6MTczMzEyNTczMX0.HuUTORcMVT8pRL9wIPU5mDgJgmx0MY0_Xydjo8nKnGo")
                .getBody();
        System.out.println(claims);
    }

}
