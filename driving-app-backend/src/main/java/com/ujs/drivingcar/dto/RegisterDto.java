package com.ujs.drivingcar.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
public class RegisterDto implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String account;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String verifycode;
    @NotBlank(message = "token不能为空")
    private String token;
}