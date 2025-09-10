package com.ujs.drivingcar.shrio;


import cn.hutool.core.bean.BeanUtil;
import com.ujs.drivingcar.pojo.User;
import com.ujs.drivingcar.service.UserService;
import com.ujs.drivingcar.utils.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * 当对用户执行认证（登录）和授权（访问控制）验证时，Shiro 会从应用配置的 Realm 中查找用户及其权限信息。
 * 从这个意义上讲，Realm 实质上是一个安全相关的 DAO：它封装了数据源的连接细节，并在需要时将相关数据提供给 Shiro 。
 *
 * Realm能做的工作主要有以下几个方面：
 *
 * 权限获取（getAuthorizationInfo 方法） 获取指定身份的权限，并返回相关信息
 *
 * 身份验证（getAuthenticationInfo 方法）验证账户和密码，并返回相关信息
 *
 * 令牌支持（supports方法）判断该令牌（Token）是否被支持
 *
 *          令牌有很多种类型，例如：HostAuthenticationToken（主机验证令牌），UsernamePasswordToken（账户密码验证令牌）
 *
 */

@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    //为了让realm支持JwtToken而不是其他Token
    @Override
    public boolean supports(AuthenticationToken token) {
        //需要这样一个判断的逻辑
        return token instanceof JwtToken;
        //如果是的话，那么下面的doGetAuthenticationInfo里的就可以强转
    }

    //权限校验
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }
    //登录认证校验
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) token;
        /**
         * jwtToken.getPrincipal()返回身份信息 getClaimByToken()的参数就是token
         *
         * getClaimByToken((String)jwtToken.getPrincipal()) 返回的是一个claim对象，用getSubject()获取userid
         * 因为在生成jwt里面有 setSubject(userId+"")
         */

//        String userid=jwtUtil.getClaimByToken((String)jwtToken.getPrincipal()).getSubject();
//        User user=userService.findUserById(userid);
//
//        if(user == null){
//            throw new UnknownAccountException("账户不存在");
//        }

//        AccountProfile profile = new AccountProfile();
//        BeanUtil.copyProperties(user,profile);

        //将user数据转移到profile
        //用户信息  密钥token 用户名字
        //return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());

        return new SimpleAuthenticationInfo();
    }

}
