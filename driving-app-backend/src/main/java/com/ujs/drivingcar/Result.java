package com.ujs.drivingcar;

import lombok.Data;

@Data
public class Result {
    // 结果状态码
    private Integer code;
    // 结果消息
    private String msg;
    // 结果数据
    private Object data;

    public static Result success(Integer code, String msg, Object data){
        Result result=new Result();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(Integer code, String msg, Object data){
        Result result=new Result();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

}
