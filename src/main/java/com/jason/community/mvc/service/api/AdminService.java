package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Admin;

import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface AdminService {

    Admin getAdminByUsername(String username);

    void addAdmin(Admin admin);

    Page<Admin> getAllAdminBySearch(Map<String, Object> searchMap);

    Admin getAdminById(Integer id);
}
