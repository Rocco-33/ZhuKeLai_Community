package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Repair;
import com.jason.community.mvc.service.api.RepairService;
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
@PreAuthorize("hasAuthority('service:write')")
@RestController
@RequestMapping("/repair")
public class RepairController {

    @Resource
    private RepairService repairService;

    /**
     * 获取所有查询到的报修信息
     */
    @PreAuthorize("hasAuthority('service:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Repair>> getAllRepairBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Repair> repairPage = repairService.getAllRepairBySearch(searchMap);
            return PageResultEntity.successWithData(repairPage, repairPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加报修信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addRepair(@RequestBody Repair repair) {
        try {
            // 执行添加业务
            repairService.addRepair(repair);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改报修信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateRepair(@RequestBody Repair repair) {
        try {
            // 执行修改业务
            repairService.updateRepair(repair);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取报修信息
     */
    @RequestMapping("/get/by/id")
    public ResultEntity<Repair> getRepairById(Integer id) {
        try {
            // 执行查询业务
            Repair repair = repairService.getRepairById(id);
            return ResultEntity.successWithData(repair);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除报修信息
     */
    @RequestMapping("/delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            repairService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
