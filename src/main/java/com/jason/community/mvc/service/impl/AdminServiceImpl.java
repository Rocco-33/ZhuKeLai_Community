package com.jason.community.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jason.community.common.CommunityConstant;
import com.jason.community.entity.Admin;
import com.jason.community.entity.AdminExample;
import com.jason.community.exception.LoginFailedException;
import com.jason.community.mvc.mapper.AdminMapper;
import com.jason.community.mvc.service.api.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 根据账号查询用户
     */
    @Override
    public Admin getAdminByUsername(String username) {

        // 1.根据登录账号查询Admin对象
        // 1.1 创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        // 1.2 创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        // 1.3 在Criteria对象中封装查询条件
        criteria.andUsernameEqualTo(username);
        // 1.4 调用AdminMapper的方法进行查询
        List<Admin> adminList = adminMapper.selectByExample(adminExample);

        // 2.判断Admin对象是否为null
        if (adminList == null || adminList.size() == 0) {
            throw new LoginFailedException(CommunityConstant.MESSAGE_LOGIN_FAILED);
        }
        if (adminList.size() > 1) { // 如果查出多个用户
            throw new LoginFailedException(CommunityConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        // 3.如果Admin对象为null则抛出异常
        Admin admin = adminList.get(0);
        if (admin == null) {
            throw new LoginFailedException(CommunityConstant.MESSAGE_LOGIN_FAILED);
        }
        // 4.如果Admin对象不为null则返回
        return admin;
    }

    /**
     * 添加用户
     */
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class,
            readOnly = false
    ) // 指定事务传播行为、回滚
    @Override
    public void addAdmin(Admin admin) {
        adminMapper.insertSelective(admin);
    }

    /**
     * 获取查询的用户对象
     */
    @Override
    public Page<Admin> getAllAdminBySearch(Map<String, Object> searchMap) {

        // E.检查 数据是否有效
        if (searchMap == null) {
            throw new RuntimeException(CommunityConstant.SEARCH_MAP_IS_NULL);
        }
        // 1.初始化查询条件
        int pageNum = 1;
        int pageSize = 2;
        // 1.根据登录账号查询Admin对象
        // 1.1 创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        // 1.2 创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();

        // 1.1 查询条件：开始-结束时间
        if (StringUtil.isNotEmpty((String) searchMap.get("startTime"))) {
            criteria.andCreateTimeGreaterThanOrEqualTo((Date) searchMap.get("startTime"));
        }
        if (StringUtil.isNotEmpty((String) searchMap.get("endTime"))) {
            criteria.andCreateTimeLessThanOrEqualTo((Date) searchMap.get("endTime"));
        }
        // 1.2 查询条件：用户名称
        if (StringUtil.isNotEmpty((String) searchMap.get("name"))) {
            criteria.andUsernameLike("%"+searchMap.get("name")+"%");
        }
        // 1.3 分页条件
        if (StringUtil.isNotEmpty(""+searchMap.get("pageNum"))) {
            pageNum = Integer.parseInt(""+searchMap.get("pageNum"));
        }
        if (StringUtil.isNotEmpty(""+searchMap.get("pageSize"))) {
            pageSize = Integer.parseInt(""+searchMap.get("pageSize"));
        }

        // 2.开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 3.根据查询条件，执行查询
        return (Page<Admin>) adminMapper.selectByExample(adminExample);
    }

    /**
     * 根据 id 查询用户对象
     */
    @Override
    public Admin getAdminById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

}
