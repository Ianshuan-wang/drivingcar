package com.ujs.drivingcar.utils;

import org.apache.commons.mail.EmailException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) throws EmailException, InterruptedException, FileNotFoundException {
        JsonUtil jsonUtil=new JsonUtil();
        String str="{\"resultcode\":\"200\",\"reason\":\"Return Successd!\",\"result\":{\"province\":\"江苏\",\"city\":\"徐州\",\"areacode\":\"0516\",\"zip\":\"\",\"company\":\"移动\",\"card\":\"\"},\"error_code\":0}";
        Map<String,Object> json_map = new HashMap<>();
        Map<String, Object> result_map=new HashMap<>();
        json_map=jsonUtil.toMap1(str);
        result_map=jsonUtil.getMap(json_map,"result");
        String province=String.valueOf(result_map.get("province"));
        String company=String.valueOf(result_map.get("company"));
        String city=String.valueOf(result_map.get("city"));
        System.out.println(province);
        System.out.println(city);
        System.out.println(company);
    }
}
