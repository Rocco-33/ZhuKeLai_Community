package com.jason.community.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jason.community.common.CommunityConstant;
import com.jason.community.entity.ParkingUse;
import com.jason.community.mvc.mapper.ParKingUseMapper;
import com.jason.community.mvc.service.api.ParkingUseService;
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
public class ParkingUseServiceImpl implements ParkingUseService {

    @Resource
    private ParKingUseMapper parKingUseMapper;

    /**
     * 获取查询的车位使用对象
     */
    @Override
    public Page<ParkingUse> getAllParkingUseBySearch(Map<String, Object> searchMap) {
        // E.检查 数据是否有效
        if (searchMap == null) {
            throw new RuntimeException(CommunityConstant.SEARCH_MAP_IS_NULL);
        }
        // 1.初始化查询条件
        int pageNum = 1;
        int pageSize = 10;
        Example example = new Example(ParkingUse.class);
        Example.Criteria criteria = example.createCriteria();

        // 1.1 查询条件：开始-结束时间
        if (StringUtil.isNotEmpty((String) searchMap.get("startTime"))) {
            criteria.andGreaterThanOrEqualTo("startTime", searchMap.get("startTime"));
        }
        if (StringUtil.isNotEmpty((String) searchMap.get("endTime"))) {
            criteria.andLessThanOrEqualTo("endTime", searchMap.get("endTime"));
        }
        // 1.2 查询条件：车位编号
        if (StringUtil.isNotEmpty((String) searchMap.get("name"))) {
            criteria.andLike("code", "%"+searchMap.get("name")+"%");
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
        return (Page<ParkingUse>) parKingUseMapper.selectByExample(example);
    }

    /**
     * 添加车位使用对象
     */
    @Override
    public void addParkingUse(ParkingUse ParkingUse) {
        parKingUseMapper.insertSelective(ParkingUse);
    }

    /**
     * 修改车位使用对象
     */
    @Override
    public void updateParkingUse(ParkingUse ParkingUse) {
        parKingUseMapper.updateByPrimaryKeySelective(ParkingUse);
    }

    /**
     * 根据 id 查询车位使用对象
     */
    @Override
    public ParkingUse getParkingUseById(Integer id) {
        return parKingUseMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改车位使用状态
     */
    @Override
    public void updateStatus(Integer id, String status) {
        ParkingUse ParkingUse = new ParkingUse();
        ParkingUse.setId(id);
        ParkingUse.setUseType(status);
        parKingUseMapper.updateByPrimaryKeySelective(ParkingUse);
    }

    /**
     * 删除车位使用信息
     */
    @Override
    public void deleteCommunities(List<Integer> ids) {
        // E.检查数据是否有效
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(CommunityConstant.DELETE_IDS_IS_NULL);
        }
        for (Integer id : ids) {
            parKingUseMapper.deleteByPrimaryKey(id);
        }
    }
    
}
