package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.House;
import com.jason.community.mvc.service.api.HouseService;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasAuthority('house:write')")
@RestController
@RequestMapping("/house")
public class HouseController {

    @Resource
    private HouseService houseService;

    /**
     * 获取所有查询到的房产信息
     */
    @PreAuthorize("hasAuthority('house:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<House>> getAllHouseBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<House> HousePage = houseService.getAllHouseBySearch(searchMap);
            return PageResultEntity.successWithData(HousePage, HousePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加房产信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addHouse(@RequestBody House House) {
        try {
            // 执行添加业务
            houseService.addHouse(House);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改房产信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateHouse(@RequestBody House House) {
        try {
            // 执行修改业务
            houseService.updateHouse(House);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取房产信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<House> getHouseById(Integer id) {
        try {
            // 执行查询业务
            House House = houseService.getHouseById(id);
            return ResultEntity.successWithData(House);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除房产信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            houseService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
