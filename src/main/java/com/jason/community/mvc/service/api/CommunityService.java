package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Community;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface CommunityService {

    Page<Community> getAllCommunityBySearch(Map<String, Object> searchMap);

    void addCommunity(Community community);

    void updateCommunity(Community community);

    Community getCommunityById(Integer id);

    void updateStatus(Integer id, String status);

    void deleteCommunities(List<Integer> ids);
}
