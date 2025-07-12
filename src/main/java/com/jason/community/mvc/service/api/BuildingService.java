package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Building;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface BuildingService {

    Page<Building> getAllBuildingBySearch(Map<String, Object> searchMap);

    void addBuilding(Building building);

    void updateBuilding(Building building);

    Building getBuildingById(Integer id);

    void deleteByIds(List<Integer> ids);
}
