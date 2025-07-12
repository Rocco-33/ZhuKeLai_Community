package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Auth;
import com.jason.community.entity.Building;
import com.jason.community.entity.Role;
import com.jason.community.mvc.service.api.AuthService;
import com.jason.community.mvc.service.api.BuildingService;
import com.jason.community.mvc.service.api.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('admin:write')")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;
    @Resource
    private AuthService authService;

    /**
     * 获取所有查询到的角色信息
     */
    @PreAuthorize("hasAuthority('admin:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Role>> getAllRoleBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Role> rolePage = roleService.getAllRoleBySearch(searchMap);
            return PageResultEntity.successWithData(rolePage, rolePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 获取所有角色信息
     */
    @RequestMapping("/get/all")
    public ResultEntity<List<Role>> getAllRole() {
        try {
            // 执行查询业务
            List<Role> roleList = roleService.getAllRole();
            return ResultEntity.successWithData(roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加角色信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addRole(@RequestBody Role role) {
        try {
            // 执行添加业务
            roleService.addRole(role);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改角色信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateRole(@RequestBody Role role) {
        try {
            // 执行修改业务
            roleService.updateRole(role);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 用户id 删除已分配角色
     */
    @RequestMapping("/remove/assigned/role/by/id")
    public ResultEntity<String> removeAssignedRoleById(Integer adminId, Integer roleId) {
        try {
            // 执行删除业务
            roleService.removeAssignedRoleById(adminId, roleId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 用户id 添加未分配角色
     */
    @RequestMapping("/add/unassigned/role/by/id")
    public ResultEntity<String> addUnassignedRoleById(String adminId, String roleId) {
        int adminId_int = Integer.parseInt(adminId);
        int roleId_int = Integer.parseInt(roleId);
        try {
            // 执行增加业务
            roleService.addUnassignedRoleById(adminId_int, roleId_int);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取角色信息
     */
    @RequestMapping("/get/by/id")
    public ResultEntity<Role> getRoleById(Integer id) {
        try {
            // 执行查询业务
            Role role = roleService.getRoleById(id);
            return ResultEntity.successWithData(role);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除角色信息
     */
    @RequestMapping("/delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            roleService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取 未分配权限信息
     */
    @ResponseBody
    @RequestMapping("/get/unassigned/auth/by/id")
    public ResultEntity<List<Auth>> getUnassignedAuthById(Integer id) {
        try {
            // 执行查询业务
            List<Auth> unassignedAuthList = authService.getUnAssignedAuth(id);
            return ResultEntity.successWithData(unassignedAuthList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取 已分配权限信息
     */
    @ResponseBody
    @RequestMapping("/get/assigned/auth/by/id")
    public ResultEntity<List<Auth>> getAssignedAuthById(Integer id) {
        try {
            // 执行查询业务
            List<Auth> assignedAuthList = authService.getAssignedAuth(id);
            return ResultEntity.successWithData(assignedAuthList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
