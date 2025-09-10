package com.ujs.drivingcar.mapper;

import com.ujs.drivingcar.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    User findUserByPhone(String phone);

    User findUserByEmail(String email);

    boolean addUser(User user);

    List<User> queryAll();
}
