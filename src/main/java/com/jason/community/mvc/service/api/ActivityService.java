package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface ActivityService {

    Page<Activity> getAllActivityBySearch(Map<String, Object> searchMap);

    void addActivity(Activity activity);

    void updateActivity(Activity activity);

    Activity getActivityById(Integer id);

    void deleteByIds(List<Integer> ids);
}
