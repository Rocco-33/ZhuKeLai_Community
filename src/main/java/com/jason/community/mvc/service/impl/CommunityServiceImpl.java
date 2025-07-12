package com.jason.community.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jason.community.common.CommunityConstant;
import com.jason.community.entity.Community;
import com.jason.community.mvc.mapper.CommunityMapper;
import com.jason.community.mvc.service.api.CommunityService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@Service
public class CommunityServiceImpl implements CommunityService {

    @Resource
    private CommunityMapper communityMapper;

    /**
     * 获取查询的小区对象
     */
    @Override
    public Page<Community> getAllCommunityBySearch(Map<String, Object> searchMap) {
        // E.检查 数据是否有效
        if (searchMap == null) {
            throw new RuntimeException(CommunityConstant.SEARCH_MAP_IS_NULL);
        }
        // 1.初始化查询条件
        int pageNum = 1;
        int pageSize = 10;
        Example example = new Example(Community.class);
        Example.Criteria criteria = example.createCriteria();

        // 1.1 查询条件：开始-结束时间
        if (StringUtil.isNotEmpty((String) searchMap.get("startTime"))) {
            criteria.andGreaterThanOrEqualTo("createTime", searchMap.get("startTime"));
        }
        if (StringUtil.isNotEmpty((String) searchMap.get("endTime"))) {
            criteria.andLessThanOrEqualTo("createTime", searchMap.get("endTime"));
        }
        // 1.2 查询条件：小区名称
        if (StringUtil.isNotEmpty((String) searchMap.get("name"))) {
            criteria.andLike("name", "%"+searchMap.get("name")+"%");
        }
        // 1.3 分页条件
        if (StringUtil.isNotEmpty(""+searchMap.get("pageNum"))) {
            pageNum = Integer.parseInt(""+searchMap.get("pageNum"));
        }
        if (StringUtil.isNotEmpty(""+searchMap.get("pageSize"))) {
            pageSize = Integer.parseInt(""+searchMap.get("pageSize"));
        }

        // 2.开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 3.根据查询条件，执行查询
        return (Page<Community>) communityMapper.selectByExample(example);
    }

    /**
     * 添加小区对象
     */
    @Override
    public void addCommunity(Community community) {
        communityMapper.insertSelective(community);
    }

    /**
     * 修改小区对象
     */
    @Override
    public void updateCommunity(Community community) {
        communityMapper.updateByPrimaryKeySelective(community);
    }

    /**
     * 根据 id 查询小区对象
     */
    @Override
    public Community getCommunityById(Integer id) {
        return communityMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改小区状态
     */
    @Override
    public void updateStatus(Integer id, String status) {
        Community community = new Community();
        community.setId(id);
        community.setStatus(status);
        communityMapper.updateByPrimaryKeySelective(community);
    }

    /**
     * 删除小区信息
     */
    @Override
    public void deleteCommunities(List<Integer> ids) {
        // E.检查数据是否有效
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(CommunityConstant.DELETE_IDS_IS_NULL);
        }
        for (Integer id : ids) {
            communityMapper.deleteByPrimaryKey(id);
        }
    }

}
