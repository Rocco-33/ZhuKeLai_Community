package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface RoleService {

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);

    Page<Role> getAllRoleBySearch(Map<String, Object> searchMap);

    void addRole(Role role);

    Role getRoleById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateRole(Role role);

    List<Role> getAllRole();

    void removeAssignedRoleById(Integer adminId, Integer roleId);

    void addUnassignedRoleById(Integer adminId, Integer roleId);
}
