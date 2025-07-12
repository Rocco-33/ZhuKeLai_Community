package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Activity;
import com.jason.community.mvc.service.api.ActivityService;
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
@PreAuthorize("hasAuthority('service:write')")
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    /**
     * 获取所有查询到的活动信息
     */
    @PreAuthorize("hasAuthority('service:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Activity>> getAllActivityBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Activity> activityPage = activityService.getAllActivityBySearch(searchMap);
            return PageResultEntity.successWithData(activityPage, activityPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加活动信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addActivity(@RequestBody Activity activity) {
        try {
            // 执行添加业务
            activityService.addActivity(activity);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改活动信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateActivity(@RequestBody Activity activity) {
        try {
            // 执行修改业务
            activityService.updateActivity(activity);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取活动信息
     */
    @RequestMapping("/get/by/id")
    public ResultEntity<Activity> getActivityById(Integer id) {
        try {
            // 执行查询业务
            Activity activity = activityService.getActivityById(id);
            return ResultEntity.successWithData(activity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除活动信息
     */
    @RequestMapping("/delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            activityService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
