package com.ujs.drivingcar.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import com.ujs.drivingcar.Result;
import com.ujs.drivingcar.dto.LoginDto;
import com.ujs.drivingcar.pojo.User;
import com.ujs.drivingcar.service.UserService;
import com.ujs.drivingcar.utils.JsonUtil;
import com.ujs.drivingcar.utils.JwtUtil;
import com.ujs.drivingcar.utils.ResponeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    JwtUtil jwtUtil;

    ResponeUtil responeUtil;
    JsonUtil jsonUtil;
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
    @PostMapping("/login")//用loginDto校验
    public Map<String,Object> login(@RequestParam Map<String, String> params, HttpServletResponse response){
        String json=params.get("json");

        Map<String,String> loginmap = new HashMap<>();
        loginmap=jsonUtil.toMap(json);

        String account=loginmap.get("account");           //登录账号
        String password=loginmap.get("password");         //登录密码

        System.out.println(account);
        System.out.println(password);
        if(account.equals("")){
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"账号不能为空",null);
        }
        if(password.equals("")){
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"密码不能为空",null);
        }
        User user;
        if(account.indexOf("@")==-1){//电话
            user=userService.findUserByPhone(account);
        }
        else{//邮箱
            user=userService.findUserByEmail(account);
        }

        Assert.notNull(user,"用户不存在");//断言拦截
        if(!user.getPassword().equals(SecureUtil.md5(password))){
            //密码不同则抛出异常
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"密码不正确",null);
        }

        //生成token
        long userid=Long.parseLong(user.getId());
        String jwt = jwtUtil.generateToken(userid);

        Map<String,Object> map = new HashMap<>(1);
        map.put("token",jwt);

        //将token 放在header里面
        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"登陆成功",map);
    }

    @GetMapping("/all")
    public List<User> queryall(){
        List<User> userList=userService.queryAll();
        return userList;
    }

}
