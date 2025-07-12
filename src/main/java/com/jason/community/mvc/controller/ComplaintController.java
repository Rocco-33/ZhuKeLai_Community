package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Complaint;
import com.jason.community.mvc.service.api.ComplaintService;
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
@RequestMapping("/complaint")
public class ComplaintController {

    @Resource
    private ComplaintService complaintService;

    /**
     * 获取所有查询到的投诉信息
     */
    @PreAuthorize("hasAuthority('service:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Complaint>> getAllComplaintBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Complaint> complaintPage = complaintService.getAllComplaintBySearch(searchMap);
            return PageResultEntity.successWithData(complaintPage, complaintPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加投诉信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addComplaint(@RequestBody Complaint complaint) {
        try {
            // 执行添加业务
            complaintService.addComplaint(complaint);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改投诉信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateComplaint(@RequestBody Complaint complaint) {
        try {
            // 执行修改业务
            complaintService.updateComplaint(complaint);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取投诉信息
     */
    @RequestMapping("/get/by/id")
    public ResultEntity<Complaint> getComplaintById(Integer id) {
        try {
            // 执行查询业务
            Complaint complaint = complaintService.getComplaintById(id);
            return ResultEntity.successWithData(complaint);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除投诉信息
     */
    @RequestMapping("/delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            complaintService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
