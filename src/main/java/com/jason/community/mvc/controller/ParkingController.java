package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Parking;
import com.jason.community.mvc.service.api.ParkingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('parking:write')")
@RestController
@RequestMapping("/parking")
public class ParkingController {

    @Resource
    private ParkingService parkingService;

    /**
     * 获取所有查询到的车位信息
     */
    @PreAuthorize("hasAuthority('parking:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Parking>> getAllParkingBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Parking> parkingPage = parkingService.getAllParkingBySearch(searchMap);
            return PageResultEntity.successWithData(parkingPage, parkingPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加车位信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addParking(@RequestBody Parking parking) {
        try {
            // 执行添加业务
            parkingService.addParking(parking);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改车位信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateParking(@RequestBody Parking parking) {
        try {
            // 执行修改业务
            parkingService.updateParking(parking);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取车位信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<Parking> getParkingById(Integer id) {
        try {
            // 执行查询业务
            Parking parking = parkingService.getParkingById(id);
            return ResultEntity.successWithData(parking);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除车位信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            parkingService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改车位状态
     */
    @RequestMapping("/update/status/{status}/{id}")
    public ResultEntity<String> updateStatus(
            @PathVariable("id") Integer id,
            @PathVariable("status") String status
    ) {
        try {
            // 执行修改业务
            parkingService.updateStatus(id, status);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
