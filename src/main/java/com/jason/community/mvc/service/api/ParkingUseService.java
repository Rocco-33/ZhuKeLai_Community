package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.ParkingUse;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface ParkingUseService {

    Page<ParkingUse> getAllParkingUseBySearch(Map<String, Object> searchMap);

    void addParkingUse(ParkingUse parkingUse);

    void updateParkingUse(ParkingUse parkingUse);

    ParkingUse getParkingUseById(Integer id);

    void updateStatus(Integer id, String status);

    void deleteCommunities(List<Integer> ids);
}
