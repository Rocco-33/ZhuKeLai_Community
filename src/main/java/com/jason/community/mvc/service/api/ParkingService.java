package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Parking;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface ParkingService {

    Page<Parking> getAllParkingBySearch(Map<String, Object> searchMap);

    void addParking(Parking parking);

    void updateParking(Parking parking);

    Parking getParkingById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateStatus(Integer id, String status);
}
