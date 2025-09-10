package com.ujs.drivingcar.shrio;

import cn.hutool.json.JSONUtil;
import com.ujs.drivingcar.utils.JwtUtil;
import com.ujs.drivingcar.utils.ResponeUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


//使用shrio内置过滤器
@Component
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //获取jwt信息
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        String jwt=request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)){
            return null;
        }
        return new JwtToken(jwt);
    }
    //拦截
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //获取jwt信息
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        String jwt=request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)){
            //当没有jwt 就不需要交给shrio进行登录处理，后面直接交给注解进行拦截就可以了
            return true;
        }
        else{
            //校验jwt
            Claims claim = jwtUtil.getClaimByToken(jwt);
            //如果为空或者过期
            if (claim == null ||jwtUtil.isTokenExpired(claim.getExpiration())) {
                throw new ExpiredCredentialsException("token失效，请重新登陆");
            }
            //执行登录
            return executeLogin(servletRequest,servletResponse);
        }
    }

    //登录异常处理
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //如果为空就返回e 不为空返回e.getCause()
        Throwable throwable=e.getCause()==null?e:e.getCause();

        ResponeUtil responeUtil=new ResponeUtil();
        Map<String,Object> map = new HashMap<>(3);
        map=responeUtil.ResponseMapJSON(responeUtil.PERMISSION_DENIED,throwable.getMessage(),null);
        String json= JSONUtil.toJsonStr(map);

        try {
            response.getWriter().print(json);
        } catch (IOException ioException) {
        }
        return false;
    }

    /**
     * 对跨域提供支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求,这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}
