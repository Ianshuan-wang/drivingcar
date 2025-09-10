package com.ujs.drivingcar.controller;


import cn.hutool.core.lang.Assert;
import com.ujs.drivingcar.pojo.CollectionPolid;
import com.ujs.drivingcar.service.CollectionService;
import com.ujs.drivingcar.service.UserSugService;
import com.ujs.drivingcar.utils.JsonUtil;
import com.ujs.drivingcar.utils.JwtUtil;
import com.ujs.drivingcar.utils.ResponeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CollectionController {

    @Autowired
    JwtUtil jwtUtil;

    ResponeUtil responeUtil;
    JsonUtil jsonUtil;
    @Autowired
    CollectionService collectionService;

    @PostMapping("/collect")
    public Map<String,Object> collect(@RequestHeader("token") String token,
                                     @RequestParam Map<String, String> params,
                                     HttpServletResponse response) throws IOException {
        String json=params.get("json");
        Map<String,String> jsonmap = new HashMap<>();
        jsonmap=jsonUtil.toMap(json);
        //获取poiId
        String poiId=jsonmap.get("poiId");
        //获取userid
        String userid=jwtUtil.getClaimByToken(token).getSubject();
        //新建坐标点对象
        CollectionPolid collectionPolid=new CollectionPolid();
        try{
            collectionPolid.setPoi_id(poiId);
            collectionPolid.setUserid(userid);
            System.out.println(collectionPolid.toString());
            //执行添加语句
            collectionService.addCollection(collectionPolid);
            return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"收藏成功",null);
        }catch (Exception e) {
            return responeUtil.ResponseMapJSON(responeUtil.DATABASE_ERROR,"收藏失败",null);
        }
    }

    @PostMapping("/uncollect")
    public Map<String,Object> uncollect(@RequestHeader("token") String token,
                                        @RequestParam Map<String, String> params,
                                        HttpServletResponse response) throws IOException {
        String json=params.get("json");
        Map<String,String> jsonmap = new HashMap<>();
        jsonmap=jsonUtil.toMap(json);
        //获取poiId
        String poiId=jsonmap.get("poiId");
        //获取userid
        String userid=jwtUtil.getClaimByToken(token).getSubject();
        //新建坐标点对象
        CollectionPolid collectionPolid=new CollectionPolid();
        try{
            collectionPolid.setPoi_id(poiId);
            collectionPolid.setUserid(userid);
            System.out.println(collectionPolid.toString());
            //执行删除语句
            collectionService.deleteCollection(collectionPolid);
            return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"删除成功",null);
        }catch (Exception e) {
            return responeUtil.ResponseMapJSON(responeUtil.DATABASE_ERROR,"删除失败",null);
        }
    }

    @GetMapping("/iscollect")
    public Map<String,Object> iscollect(@RequestHeader("token") String token,
                                        @RequestParam("poiId") String poiId,
                                         HttpServletResponse response) throws IOException {
        //获取userid
        String userid=jwtUtil.getClaimByToken(token).getSubject();
        System.out.println(token);
        System.out.println(userid);
        //新建坐标点对象
        CollectionPolid collectionPolid=new CollectionPolid();
        collectionPolid.setPoi_id(poiId);
        collectionPolid.setUserid(userid);
        System.out.println(collectionPolid.toString());
        //执行查询语句
        CollectionPolid result=collectionService.existCollection(collectionPolid);

        Assert.notNull(result,"未收藏");//断言拦截
        return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"已收藏",null);
    }

    @GetMapping("/getallcollect")
    public Map<String,Object> getallcollect(@RequestHeader("token") String token,HttpServletResponse response) throws IOException {
        //获取userid
        String userid=jwtUtil.getClaimByToken(token).getSubject();

        //新建坐标点对象
        CollectionPolid collectionPolid=new CollectionPolid();

        try{

            collectionPolid.setUserid(userid);
            System.out.println(collectionPolid.toString());
            //执行添加语句
            List<CollectionPolid> collectionPolidList=collectionService.getallcollect(collectionPolid);
            Map<String,Object> polidmap=new HashMap<>();
            List<String> polidlist=new ArrayList<>();
            for (CollectionPolid polid : collectionPolidList) {
                polidlist.add(polid.getPoi_id());
            }
            polidmap.put("poiId",polidlist);

            return responeUtil.ResponseMapJSON(responeUtil.SUCCESS,"查询成功",polidmap);
        }catch (Exception e) {
            return responeUtil.ResponseMapJSON(responeUtil.DATABASE_ERROR,"查询失败",null);
        }

    }
}
