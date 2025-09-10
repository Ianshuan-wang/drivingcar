package com.ujs.drivingcar.shrio;
import lombok.Data;

import java.io.Serializable;

//创建类AccountProfile 用于传递数据
@Data
public class AccountProfile implements Serializable {
    private String id;

    private String username;

}
