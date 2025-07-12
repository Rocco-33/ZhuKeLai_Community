package com.jason.community.config;

import com.jason.community.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * SpringSecurity提供的用来封装用户对象 User，只包含账号和密码
 * 为了获取原始的 Admin对象，专门创建该类对 User对象进行扩展
 *
 * @author Jason
 * @version 1.0
 */
public class CrowdSecurityAdmin extends User {

    private static final long serialVersionUID = 1L;

    private Admin originalAdmin; //原始的 Admin对象，包含其全部属性

    /**
     * 构造器
     *
     * @param originalAdmin 原始的 Admin对象
     * @param authorities 角色、权限信息的集合
     */
    public CrowdSecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        // 调用父类构造器
        super(originalAdmin.getUsername(), originalAdmin.getPassword(), authorities);
        // 初始化 原始Admin对象
        this.originalAdmin = originalAdmin;
        // 该类以 principal（主体）属性名保存到了 UsernamePasswordAuthenticationToken
        // 效仿其擦除隐私的方法 eraseCredentials()，也将聚合的原始admin的密码擦除
        this.originalAdmin.setPassword(null);
    }

    /**
     * 获取原始Admin对象
     */
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
