package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Auth;
import com.jason.community.mvc.service.api.AuthService;
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
@PreAuthorize("hasAuthority('admin:write')")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * 获取所有查询到的权限信息
     */
    @PreAuthorize("hasAuthority('admin:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Auth>> getAllAuthBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Auth> AuthPage = authService.getAllAuthBySearch(searchMap);
            return PageResultEntity.successWithData(AuthPage, AuthPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 获取所有权限信息
     */
    @RequestMapping("/get/all")
    public ResultEntity<List<Auth>> getAllAuth() {
        try {
            // 执行查询业务
            List<Auth> authList = authService.getAllAuth();
            return ResultEntity.successWithData(authList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加权限信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addAuth(@RequestBody Auth auth) {
        try {
            // 执行添加业务
            authService.addAuth(auth);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改权限信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateAuth(@RequestBody Auth auth) {
        try {
            // 执行修改业务
            authService.updateAuth(auth);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据角色id，删除已分配权限
     */
    @RequestMapping("/remove/assigned/auth/by/id")
    public ResultEntity<String> removeAssignedAuthById(Integer roleId, Integer authId) {
        try {
            // 执行删除业务
            authService.removeAssignedAuthById(roleId, authId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据角色id，添加未分配权限
     */
    @RequestMapping("/add/unassigned/auth/by/id")
    public ResultEntity<String> addUnassignedAuthById(String roleId, String authId) {
        int roleId_int = Integer.parseInt(roleId);
        int authId_int = Integer.parseInt(authId);
        try {
            // 执行增加业务
            authService.addUnassignedAuthById(roleId_int, authId_int);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取权限信息
     */
    @RequestMapping("/get/by/id")
    public ResultEntity<Auth> getAuthById(Integer id) {
        try {
            // 执行查询业务
            Auth auth = authService.getAuthById(id);
            return ResultEntity.successWithData(auth);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除权限信息
     */
    @RequestMapping("/delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            authService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
