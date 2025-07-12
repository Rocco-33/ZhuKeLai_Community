package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.House;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface HouseService {

    Page<House> getAllHouseBySearch(Map<String, Object> searchMap);

    void addHouse(House house);

    void updateHouse(House house);

    House getHouseById(Integer id);

    void deleteByIds(List<Integer> ids);
}
