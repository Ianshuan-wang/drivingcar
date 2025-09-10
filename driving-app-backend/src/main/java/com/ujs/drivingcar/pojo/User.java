package com.ujs.drivingcar.pojo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class User {
    //主键id
    private String id;
    //用户名
    private String username;
    //头像
    private String password;

    //电话
    private String phone;
    //邮箱
    private String email;

    //头像
    private String head_protrait;
    //qq
    private String qq;
    //微信
    private String wechat;

    //经验值
    private int expirical_value;
    //注册时间
    private String registration_time;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHead_protrait() {
        return head_protrait;
    }

    public void setHead_protrait(String head_protrait) {
        this.head_protrait = head_protrait;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public int getExpirical_value() {
        return expirical_value;
    }

    public void setExpirical_value(int expirical_value) {
        this.expirical_value = expirical_value;
    }

    public String getRegistration_time() {
        return registration_time;
    }

    public void setRegistration_time(String registration_time) {
        this.registration_time = registration_time;
    }
}
