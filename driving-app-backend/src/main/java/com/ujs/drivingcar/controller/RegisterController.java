package com.ujs.drivingcar.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import com.ujs.drivingcar.Result;
import com.ujs.drivingcar.dto.LoginDto;
import com.ujs.drivingcar.dto.RegisterDto;
import com.ujs.drivingcar.dto.VerifycodeDto;
import io.jsonwebtoken.*;
import com.ujs.drivingcar.pojo.User;
import com.ujs.drivingcar.service.UserService;
import com.ujs.drivingcar.utils.JsonUtil;
import com.ujs.drivingcar.utils.JwtUtil;
import com.ujs.drivingcar.utils.ResponeUtil;
import com.ujs.drivingcar.utils.SnowIdUtil;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class RegisterController {
    @Autowired
    JwtUtil jwtUtil;
    JsonUtil jsonUtil;
    ResponeUtil responeUtil;
    @Autowired
    UserService userService;

    /**
     *
     *@RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
     * GET方式无请求体，所以使用@RequestBody接收数据时，
     * 前端不能使用GET方式提交数据，
     * 而是用POST方式进行提交。在后端的同一个接收方法里，
     * @RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，
     * 而@RequestParam()可以有多个。
     *
     * @Validated注解用于检查user中填写的规则  如果不满足抛出异常
     * 可在GlobalExceptionHandler中捕获此异常 进行自定义 返回数据信息
     */


    @PostMapping("/verifycode")//
    public Map<String,Object> verifycode(@RequestParam Map<String, String> params, HttpServletResponse response) throws EmailException {
        String json=params.get("json");

        Map<String,String> loginmap = new HashMap<>();
        loginmap=jsonUtil.toMap(json);

        String account=loginmap.get("account");           //注册账号
        String password=loginmap.get("password");         //注册密码
        if(account.equals("")){
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"账号不能为空",null);
        }
        if(password.equals("")){
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"密码不能为空",null);
        }

        //生成验证码
        Random random = new Random();
        String random_num="";
        for (int i=0;i<6;i++) {
            random_num+=random.nextInt(10);
        }

        if(account.indexOf("@")==-1){//电话
            String telRegex = "[1][3578]\\d{9}";
            if(!account.matches(telRegex)){
                return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"手机号不合法",null);
            }else{
                return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"手机号验证码未实现",null);
            }
        }
        else{//邮箱
            HtmlEmail email=new HtmlEmail();//创建一个HtmlEmail实例对象
            email.setHostName("smtp.qq.com");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
            email.setCharset("utf-8");//设置发送的字符类型

            email.setSSLOnConnect(true);   //使用ssl加密true

            //阿里云25端口一般不开放
            email.setSslSmtpPort("465");
            email.addTo(account);//设置收件人

            email.setFrom("1975037886@qq.com","车旅迹忆");//发送人的邮箱为自己的，用户名可以随便填
            email.setAuthentication("1975037886@qq.com","fdljzkpffwcgfbge");//设置发送人到的邮箱和用户名和授权码

            email.setSubject("车旅迹忆-验证码");//设置发送主题
            email.setMsg("【车旅迹忆】验证码: "+random_num+",用于登录车旅迹忆邮箱账号，5分钟内有效（若非本人操作，请删除本邮件）");//设置发送内容
            email.send();//进行发送
        }

        //生成token
        long vericode=Long.parseLong(random_num);
        String jwt = jwtUtil.generateToken(vericode);
        response.setHeader("token",jwt);
        //传递data的map
        Map<String,Object> map = new HashMap<>(1);
        map.put("token",jwt);

        return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"获取验证码成功",map);
    }

    @PostMapping("/register")//用loginDto校验
    public Map<String,Object> register(@RequestParam Map<String, String> params, HttpServletResponse response){
        String json=params.get("json");

        Map<String,String> loginmap = new HashMap<>();
        loginmap=jsonUtil.toMap(json);

        String account=loginmap.get("account");           //注册账号
        String password=loginmap.get("password");         //注册密码
        String verifycode=loginmap.get("verifycode");     //注册账号
        String token=loginmap.get("token");               //注册密码
        if(account.equals("")){
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"账号不能为空",null);
        }
        if(password.equals("")){
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"密码不能为空",null);
        }
        Date date = new Date();
        //验证码是否失效
        if(jwtUtil.isVerifycodeTokenExpired(jwtUtil.getClaimByToken(token).getIssuedAt())){
            return responeUtil.ResponseMapJSON(responeUtil.PERMISSION_DENIED,"验证码失效",null);
        }


        //雪花算法生成sid
        SnowIdUtil idWorker = new SnowIdUtil(0, 0);
        long id = idWorker.nextId();
        String sid=id+"";

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        User user=new User();
        user.setId(sid);
        user.setExpirical_value(0);
        user.setRegistration_time(ft.format(date));

        if(account.indexOf("@")==-1){//电话
            user.setPhone(account);
        }
        else{//邮箱
            user.setEmail(account);
        }
        user.setPassword(SecureUtil.md5(password));

        //获取registerDto.token验证 verifycode
        System.out.println(verifycode);
        String tokencode=jwtUtil.getClaimByToken(token).getSubject();


        Result result=new Result();
        if(tokencode.equals(verifycode)){
            long userid=Long.parseLong(user.getId());
            String jwt = jwtUtil.generateToken(userid);
            //传递data的map
            Map<String,Object> map = new HashMap<>(1);
            map.put("token",jwt);
            try{
                userService.addUser(user);
                return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"注册成功",map);
            }catch (Exception e) {
                return responeUtil.ResponseMapJSON(responeUtil.DATABASE_ERROR,"注册失败",null);
            }
        }
        else{
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"验证码错误",null);
        }
    }

    @PostMapping("/verifytoken")//用loginDto校验
    public Map<String,Object> verifytoken(@RequestParam Map<String, String> params, HttpServletResponse response){
        String json=params.get("json");
        Map<String,String> verifytoken_map = new HashMap<>();
        verifytoken_map=jsonUtil.toMap(json);
        String token=verifytoken_map.get("token");
        Date date = new Date();
        //验证码是否失效

        try {
            Claims claims=Jwts.parser().setSigningKey("drivingcar-app").parseClaimsJws(token).getBody();

            if(jwtUtil.isTokenExpired(claims.getIssuedAt())){
                return responeUtil.ResponseMapJSON(responeUtil.PERMISSION_DENIED,"失效",null);
            }
        }  catch (UnsupportedJwtException e) {
            return responeUtil.ResponseMapJSON(responeUtil.PERMISSION_DENIED,"不支持的JWT",null);
        } catch (MalformedJwtException e) {
            return responeUtil.ResponseMapJSON(responeUtil.PERMISSION_DENIED,"解析失败",null);
        }
        catch (Exception e) {
            return responeUtil.ResponseMapJSON(responeUtil.PERMISSION_DENIED,"解析异常",null);
        }
        return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"token验证有效",null);
    }
}
