package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Building;
import com.jason.community.mvc.service.api.BuildingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('house:write')")
@RestController
@RequestMapping("/building")
public class BuildingController {

    @Resource
    private BuildingService buildingService;

    /**
     * 获取所有查询到的楼栋信息
     */
    @PreAuthorize("hasAuthority('house:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Building>> getAllBuildingBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Building> BuildingPage = buildingService.getAllBuildingBySearch(searchMap);
            return PageResultEntity.successWithData(BuildingPage, BuildingPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加楼栋信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addBuilding(@RequestBody Building Building) {
        try {
            // 执行添加业务
            buildingService.addBuilding(Building);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改楼栋信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateBuilding(@RequestBody Building Building) {
        try {
            // 执行修改业务
            buildingService.updateBuilding(Building);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取楼栋信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<Building> getBuildingById(Integer id) {
        try {
            // 执行查询业务
            Building Building = buildingService.getBuildingById(id);
            return ResultEntity.successWithData(Building);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除楼栋信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            buildingService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
