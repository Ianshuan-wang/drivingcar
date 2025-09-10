package com.ujs.drivingcar.service;

import com.ujs.drivingcar.mapper.UserMapper;
import com.ujs.drivingcar.mapper.UserSugMapper;
import com.ujs.drivingcar.pojo.UserSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSugService {
    @Autowired
    private UserSugMapper userSugMapper;

    public boolean addUserSug(UserSuggestion userSuggestion){
        userSugMapper.addUserSug(userSuggestion);
        return true;
    }
}
