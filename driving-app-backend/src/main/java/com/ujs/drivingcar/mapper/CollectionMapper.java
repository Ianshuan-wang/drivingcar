package com.ujs.drivingcar.mapper;


import com.ujs.drivingcar.pojo.CollectionPolid;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CollectionMapper {
    CollectionPolid existCollection(CollectionPolid collectionPolid);

    boolean addCollection(CollectionPolid collectionPolid);

    boolean deleteCollection(CollectionPolid collectionPolid);

    List<CollectionPolid> getallcollect(CollectionPolid collectionPolid);
}
