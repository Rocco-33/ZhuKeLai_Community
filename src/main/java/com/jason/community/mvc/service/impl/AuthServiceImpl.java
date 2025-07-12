package com.jason.community.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jason.community.common.CommunityConstant;
import com.jason.community.entity.Auth;
import com.jason.community.entity.AuthExample;
import com.jason.community.mvc.mapper.AuthMapper;
import com.jason.community.mvc.service.api.AuthService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthMapper authMapper;

    /**
     * 获取查询的权限对象
     */
    @Override
    public Page<Auth> getAllAuthBySearch(Map<String, Object> searchMap) {

        // E.检查 数据是否有效
        if (searchMap == null) {
            throw new RuntimeException(CommunityConstant.SEARCH_MAP_IS_NULL);
        }
        // 1.初始化查询条件
        int pageNum = 1;
        int pageSize = 2;
        AuthExample AuthExample = new AuthExample();
        AuthExample.Criteria criteria = AuthExample.createCriteria();
        criteria.andNameIsNotNull();

        // 1.1 查询条件：权限名称
        if (StringUtil.isNotEmpty((String) searchMap.get("name"))) {
            criteria.andTitleLike("%"+searchMap.get("name")+"%");
        }
        // 1.2 分页条件
        if (StringUtil.isNotEmpty(""+searchMap.get("pageNum"))) {
            pageNum = Integer.parseInt(""+searchMap.get("pageNum"));
        }
        if (StringUtil.isNotEmpty(""+searchMap.get("pageSize"))) {
            pageSize = Integer.parseInt(""+searchMap.get("pageSize"));
        }

        // 2.开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 3.根据查询条件，执行查询
        return (Page<Auth>) authMapper.selectByExample(AuthExample);
    }

    /**
     * 获取所有权限信息
     */
    @Override
    public List<Auth> getAllAuth() {
        return authMapper.selectAllAuth();
    }

    /**
     * 根据 角色id 删除已分配权限
     */
    @Override
    public void removeAssignedAuthById(Integer roleId, Integer AuthId) {
        authMapper.deleteRoleRel(roleId, AuthId);
    }

    /**
     * 根据 角色id 添加未分配权限
     */
    @Override
    public void addUnassignedAuthById(Integer roleId, Integer AuthId) {
        authMapper.insertRoleRel(roleId, AuthId);
    }

    /**
     * 添加权限对象
     */
    @Override
    public void addAuth(Auth Auth) {
        authMapper.insertSelective(Auth);
    }

    /**
     * 修改权限对象
     */
    @Override
    public void updateAuth(Auth Auth) {
        authMapper.updateByPrimaryKeySelective(Auth);
    }

    /**
     * 根据 id 查询权限对象
     */
    @Override
    public Auth getAuthById(Integer id) {
        return authMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除权限信息
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        // E.检查数据是否有效
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(CommunityConstant.DELETE_IDS_IS_NULL);
        }
        for (Integer id : ids) {
            authMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 查询已分配的权限
     */
    @Override
    public List<Auth> getAssignedAuth(Integer roleId) {
        return authMapper.selectAssignedAuth(roleId);
    }

    /**
     * 查询未分配的权限
     */
    @Override
    public List<Auth> getUnAssignedAuth(Integer roleId) {
        return authMapper.selectUnAssignedAuth(roleId);
    }

    /**
     * 根据 用户id 查询已分配权限名称
     */
    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
    
}
