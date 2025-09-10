package com.ujs.drivingcar.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")
public class JwtUtil {

    private String secret;
    private long expire;
    private long verifytime;
    private String header;

    /**
     * 生成jwt token
     * 在登录的时候调用
     */
    public String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire);


        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId+"")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    /**
     * 校验jwt token
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date sendtime) {
        Date nowdate=new Date();
        System.out.println(nowdate.getTime());
        System.out.println(sendtime.getTime());
        System.out.println(expire);

        if((nowdate.getTime()-sendtime.getTime())>expire){//过期
            return true;
        }
        else{//未过期
            return false;
        }
    }
    public boolean isVerifycodeTokenExpired(Date sendtime) {
        Date nowdate=new Date();
        if((nowdate.getTime()-sendtime.getTime())>verifytime){
            return true;
        }
        else{
            return false;
        }
    }
}
