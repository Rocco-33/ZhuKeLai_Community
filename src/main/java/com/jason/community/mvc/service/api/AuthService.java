package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Auth;
import com.jason.community.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface AuthService {

    List<Auth> getAssignedAuth(Integer roleId);

    List<Auth> getUnAssignedAuth(Integer roleId);

    Page<Auth> getAllAuthBySearch(Map<String, Object> searchMap);

    void addAuth(Auth Auth);

    Auth getAuthById(Integer id);

    void deleteByIds(List<Integer> ids);

    void updateAuth(Auth Auth);

    List<Auth> getAllAuth();

    void removeAssignedAuthById(Integer roleId, Integer AuthId);

    void addUnassignedAuthById(Integer roleId, Integer AuthId);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
