package com.ujs.drivingcar.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResponeUtil {
    public final static Integer SUCCESS = 200;
    public final static Integer UNKNOW_ERROR = 600;
    public final static Integer PERMISSION_DENIED = 601;
    public final static Integer DATABASE_ERROR = 602;
    public final static Integer INVALID_DATA = 603;
    public final static Integer OTHER = 604;

    public static Map<String,Object> ResponseMapJSON(Integer code,String msg, Map<String,Object> data){
        Map<String,Object> JSON = new HashMap<>(3);
        JSON.put("code",code);
        JSON.put("msg",msg);
        JSON.put("data",data);
        return JSON;
    }


    public static Map<String,Object> ResponseListJSON(Integer code, String msg, List<Map<String,Object>> data){
        Map<String,Object> JSON = new HashMap<>(3);
        JSON.put("code",code);
        JSON.put("msg",msg);
        JSON.put("data",data);
        return JSON;
    }
}
