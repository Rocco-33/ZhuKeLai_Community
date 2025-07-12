package com.jason.community.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jason.community.common.CommunityConstant;
import com.jason.community.entity.*;
import com.jason.community.mvc.mapper.RoleMapper;
import com.jason.community.mvc.service.api.RoleService;
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
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    /**
     * 获取查询的角色对象
     */
    @Override
    public Page<Role> getAllRoleBySearch(Map<String, Object> searchMap) {

        // E.检查 数据是否有效
        if (searchMap == null) {
            throw new RuntimeException(CommunityConstant.SEARCH_MAP_IS_NULL);
        }
        // 1.初始化查询条件
        int pageNum = 1;
        int pageSize = 2;
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();

        // 1.1 查询条件：角色名称
        if (StringUtil.isNotEmpty((String) searchMap.get("name"))) {
            criteria.andNameLike("%"+searchMap.get("name")+"%");
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
        return (Page<Role>) roleMapper.selectByExample(roleExample);
    }

    /**
     * 获取所有角色信息
     */
    @Override
    public List<Role> getAllRole() {
        return roleMapper.selectByExample(new RoleExample());
    }

    /**
     * 根据 用户id 删除已分配角色
     */
    @Override
    public void removeAssignedRoleById(Integer adminId, Integer roleId) {
        roleMapper.deleteAdminRel(adminId, roleId);
    }

    /**
     * 根据 用户id 添加未分配角色
     */
    @Override
    public void addUnassignedRoleById(Integer adminId, Integer roleId) {
        roleMapper.insertAdminRel(adminId, roleId);
    }

    /**
     * 添加角色对象
     */
    @Override
    public void addRole(Role Role) {
        roleMapper.insertSelective(Role);
    }

    /**
     * 修改角色对象
     */
    @Override
    public void updateRole(Role Role) {
        roleMapper.updateByPrimaryKeySelective(Role);
    }

    /**
     * 根据 id 查询角色对象
     */
    @Override
    public Role getRoleById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除角色信息
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        // E.检查数据是否有效
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(CommunityConstant.DELETE_IDS_IS_NULL);
        }
        for (Integer id : ids) {
            roleMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 查询已分配的角色
     */
    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return roleMapper.selectAssignedRole(adminId);
    }

    /**
     * 查询未分配的角色
     */
    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selectUnAssignedRole(adminId);
    }


}
