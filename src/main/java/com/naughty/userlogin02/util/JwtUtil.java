package com.naughty.userlogin02.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@Component
@Slf4j
@Data
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "sport.data")
public class JwtUtil {
    @Value("${expire}")
    private long expire;//过期时间
    @Value("${secret")
    private String secret;//秘钥
    /**
     * 生成jwt token
     */
    public String generateToken(int userId){
        Date nowDate = new Date();
        //token过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
//        return Jwts.builder()
//                .setHeaderParam("typ","JWT")
//                .setSubject(userId+"")
//                .setIssuedAt(nowdata)//当前jwt生成时间
//                .setExpiration(expireDate)//jwt失效时间
//                .signWith(SignatureAlgorithm.ES512,secret)//秘钥处理
//                .compact();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId+"")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    //解析token
    public Claims getClaimByToken(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error ", e);
            return null;
        }
    }
    //验证过期时间
    public boolean isTokenExpira(Date expiration){
        return expiration.before(new Date());
    }

}
