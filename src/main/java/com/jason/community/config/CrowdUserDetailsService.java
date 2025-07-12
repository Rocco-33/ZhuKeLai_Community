package com.jason.community.config;

import com.jason.community.entity.Admin;
import com.jason.community.entity.Role;
import com.jason.community.mvc.service.api.AdminService;
import com.jason.community.mvc.service.api.AuthService;
import com.jason.community.mvc.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jason
 * @version 1.0
 */
@Component
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1.通过username，查询Admin对象
        Admin admin = adminService.getAdminByUsername(username);

        // 2.根据Admin对象，获取adminId
        Integer adminId = admin.getId();
        // 3.通过adminId，查询角色信息
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        // 4.通过adminId，查询权限信息
        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);

        // 5.创建角色、权限信息的集合
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 6.遍历角色信息，保存到集合
        for (Role role : assignedRoleList) {
            // 手动添加 "ROLE_" 前缀，Security框架需要区分
            String roleName = "ROLE_" + role.getName();
            authorities.add(new SimpleGrantedAuthority(roleName));
        }
        // 7.遍历权限信息，保存到集合
        for (String authName : authNameList) {
            authorities.add(new SimpleGrantedAuthority(authName));
        }
        // 8.封装自定义用户对象，并返回
        return new CrowdSecurityAdmin(admin, authorities);
    }
}
