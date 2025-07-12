package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.CommunityConstant;
import com.jason.community.common.CommunityUtil;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.config.ShortMessageProperties;
import com.jason.community.entity.Admin;
import com.jason.community.entity.Role;
import com.jason.community.mvc.service.api.AdminService;
import com.jason.community.mvc.service.api.RoleService;
import com.jason.community.mvc.service.impl.RedisService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('admin:write')")
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;

    @Resource // 装配短信接口配置类
    private ShortMessageProperties shortMessageProperties;

    @Resource // 装配 Redis服务
    private RedisService redisService;

    /**
     * 根据手机号，请求接收验证码
     */
    @ResponseBody
    @RequestMapping("/send/short/message")
    public ResultEntity<String> sendMessage(@RequestParam("telephone") String telephone) {

        // 1.发送短信获取验证码
        ResultEntity<String> sendMsgResultEntity = CommunityUtil.sendShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(),
                shortMessageProperties.getAppcode(),
                telephone,
                shortMessageProperties.getTemplate_id()
        );
        // E.检查 短信发送结果
        if (!sendMsgResultEntity.getResult()) {
            return ResultEntity.failed(sendMsgResultEntity.getMessage());
        }
        // 2.获取验证码
        String code = sendMsgResultEntity.getData();
        // 3.拼接Redis保存验证码的Key
        String key = CommunityConstant.REDIS_CODE_PREFIX + telephone;

        // 4.调用 Redis服务，保存验证码（有效期 5 分钟）
        ResultEntity<String> saveCodeResultEntity =
                redisService.setRedisKeyValueWithTimeout(key, code, (long) 5, TimeUnit.MINUTES);

        // E.检查 Redis服务调用
        if (!saveCodeResultEntity.getResult()) {
            return ResultEntity.failed(saveCodeResultEntity.getMessage());
        }
        return ResultEntity.successWithoutData();
    }

    /**
     * 用户注册
     */
    @RequestMapping("/do/register")
    public String register(Admin admin,@RequestParam("code") String formCode, ModelMap modelMap) {

        // 1.获取用户输入的手机号
        String telephone = admin.getTelephone();

        // 2.拼接Redis保存验证码的Key
        String key = CommunityConstant.REDIS_CODE_PREFIX + telephone;

        // 3.远程调用Redis服务，获取Value
        ResultEntity<String> redisResultEntity = redisService.getRedisValue(key);

        // E.检查 Redis服务调用
        if (!redisResultEntity.getResult()) {
            modelMap.addAttribute(CommunityConstant.ATTR_NAME_EXCEPTION, redisResultEntity.getMessage());
            return "redirect:/register.html"; // 回到注册界面
        }
        // 4.获取 Redis保存的验证码
        String redisCode = redisResultEntity.getData();

        // E.检查 Redis存在验证码
        if (redisCode == null) {
            modelMap.addAttribute(CommunityConstant.ATTR_NAME_EXCEPTION, CommunityConstant.MESSAGE_CODE_NOT_EXIST);
            return "redirect:/register.html"; // 回到注册界面
        }
        // E.检查 比较用户输入的验证码
        if (formCode == null || !Objects.equals(formCode, redisCode)) {
            modelMap.addAttribute(CommunityConstant.ATTR_NAME_EXCEPTION, CommunityConstant.MESSAGE_CODE_INVALID);
            return "redirect:/register.html"; // 回到注册界面
        }
        // 5.验证码一致，则从Redis删除
        redisService.delRedisKey(key);

        // 6.1 获取用户输入的密码
        String rawPassword = admin.getPassword();
        // 6.2 执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(rawPassword);
        admin.setPassword(encode);

        // 7.3 调用MySQL服务，执行保存
        try {
            adminService.addAdmin(admin);
            // 请求重定向避免表单重复提交
            return "redirect:/login.html";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register.html"; // 回到注册界面
        }
    }

//    /**
//     * 用户登录
//     */
//    @RequestMapping("/admin/do/login")
//    public void login(@RequestParam("username") String username,HttpSession session) {
//        // 1.根据用户名查询用户
//        Admin admin = adminService.getAdminByUsername(username);
//        // 2.将用户保存到session域
//        session.setAttribute(CommunityConstant.ATTR_NAME_LOGIN_ADMIN, admin);
//    }

    /**
     * 获取所有查询到的用户信息
     */
    @ResponseBody
    @PreAuthorize("hasAuthority('admin:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Admin>> getAllAdminBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Admin> AdminPage = adminService.getAllAdminBySearch(searchMap);
            return PageResultEntity.successWithData(AdminPage, AdminPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取用户信息
     */
    @ResponseBody
    @RequestMapping("/get/by/id")
    public ResultEntity<Admin> getAdminById(Integer id) {
        try {
            // 执行查询业务
            Admin Admin = adminService.getAdminById(id);
            return ResultEntity.successWithData(Admin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取 未分配角色信息
     */
    @ResponseBody
    @RequestMapping("/get/unassigned/role/by/id")
    public ResultEntity<List<Role>> getUnassignedRoleById(Integer id) {
        try {
            // 执行查询业务
            List<Role> unassignedRoleList = roleService.getUnAssignedRole(id);
            return ResultEntity.successWithData(unassignedRoleList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取 已分配角色信息
     */
    @ResponseBody
    @RequestMapping("/get/assigned/role/by/id")
    public ResultEntity<List<Role>> getAssignedRoleById(Integer id) {
        try {
            // 执行查询业务
            List<Role> assignedRoleList = roleService.getAssignedRole(id);
            return ResultEntity.successWithData(assignedRoleList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
