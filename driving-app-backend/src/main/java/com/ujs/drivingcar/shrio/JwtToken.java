package com.ujs.drivingcar.shrio;

import org.apache.shiro.authc.AuthenticationToken;

//AuthenticationToken 用于收集用户提交的身份（如用户名）及凭据（如密码）：
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String jwt){
        this.token=jwt;
    }

    //获取权限
    /**
     * 返回身份信息，相当于用户的用户名
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    //获取密钥
    /**
     * 返回用户凭证信息，相当于用户的用户密码
     */
    @Override
    public Object getCredentials() {
        return token;
    }
}
