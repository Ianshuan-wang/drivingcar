package com.ujs.drivingcar.mapper;

import com.ujs.drivingcar.pojo.User;
import com.ujs.drivingcar.pojo.UserSuggestion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserSugMapper {
    boolean addUserSug(UserSuggestion userSuggestion);
}
