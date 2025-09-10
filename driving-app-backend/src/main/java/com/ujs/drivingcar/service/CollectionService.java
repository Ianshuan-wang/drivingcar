package com.ujs.drivingcar.service;



import com.ujs.drivingcar.mapper.CollectionMapper;
import com.ujs.drivingcar.pojo.CollectionPolid;
import com.ujs.drivingcar.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CollectionService {
    @Autowired
    CollectionMapper collectionMapper;
    public CollectionPolid existCollection(CollectionPolid collectionPolid){
        System.out.println("existCollection");
        return  collectionMapper.existCollection(collectionPolid);
    }

    public boolean addCollection(CollectionPolid collectionPolid){
        System.out.println("CollectionService");
        collectionMapper.addCollection(collectionPolid);
        return true;
    }

    public boolean deleteCollection(CollectionPolid collectionPolid){
        collectionMapper.deleteCollection(collectionPolid);
        return true;
    }

    public List<CollectionPolid> getallcollect(CollectionPolid collectionPolid){
        return collectionMapper.getallcollect(collectionPolid);
    }
}
