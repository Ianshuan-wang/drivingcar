package com.ujs.drivingcar.controller;

import com.ujs.drivingcar.pojo.User;
import com.ujs.drivingcar.pojo.UserSuggestion;
import com.ujs.drivingcar.service.UserService;
import com.ujs.drivingcar.service.UserSugService;
import com.ujs.drivingcar.utils.JsonUtil;
import com.ujs.drivingcar.utils.JwtUtil;
import com.ujs.drivingcar.utils.ResponeUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ClassUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class AdviceController {
    @Autowired
    JwtUtil jwtUtil;

    ResponeUtil responeUtil;
    JsonUtil jsonUtil;
    @Autowired
    UserSugService userSugService;

    String uploadPathImg="/www/server/nodeServer/public/images/";

    @PostMapping("/advice")
    public Map<String,Object> advice(@RequestHeader("token") String token, @RequestParam("image") MultipartFile[] files, @RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        if(files.length>4){
            return responeUtil.ResponseMapJSON(responeUtil.INVALID_DATA,"图片至多4张",null);
        }

        List<String> picture=new ArrayList<String>();
        for (MultipartFile file : files) {    //循环保存文件
            //文件名_时间戳
            String fileName=System.currentTimeMillis()+".png";
            System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath());
            System.out.println(fileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(uploadPathImg,fileName));
            picture.add(fileName);
        }
        String userid=jwtUtil.getClaimByToken(token).getSubject();
        String type=params.get("type");
        String advice=params.get("advice");
        System.out.println(type);
        System.out.println(advice);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        //用户建议类
        UserSuggestion userSuggestion=new UserSuggestion();
        userSuggestion.setUserid(userid);
        userSuggestion.setType(type);
        userSuggestion.setTime(ft.format(new Date()));
        userSuggestion.setContent(advice);
        userSuggestion.setPicture(picture.toString());

        System.out.println(userSuggestion.toString());
        try{
            userSugService.addUserSug(userSuggestion);
            return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"提交成功",null);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return responeUtil.ResponseMapJSON(responeUtil.DATABASE_ERROR,"失败",null);
        }
    }

}