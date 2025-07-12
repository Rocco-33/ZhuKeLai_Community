package com.jason.community.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jason.community.common.CommunityConstant;
import com.jason.community.entity.Car;
import com.jason.community.mvc.mapper.CarMapper;
import com.jason.community.mvc.service.api.CarService;
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
public class CarServiceImpl implements CarService {

    @Resource
    private CarMapper carMapper;

    /**
     * 获取查询的车辆对象
     */
    @Override
    public Page<Car> getAllCarBySearch(Map<String, Object> searchMap) {

        // E.检查 数据是否有效
        if (searchMap == null) {
            throw new RuntimeException(CommunityConstant.SEARCH_MAP_IS_NULL);
        }
        // 1.初始化查询条件
        int pageNum = 1;
        int pageSize = 2;
        Example example = new Example(Car.class);
        Example.Criteria criteria = example.createCriteria();

        // 1.1 查询条件：开始-结束时间
        if (StringUtil.isNotEmpty((String) searchMap.get("startTime"))) {
            criteria.andGreaterThanOrEqualTo("createTime", searchMap.get("startTime"));
        }
        if (StringUtil.isNotEmpty((String) searchMap.get("endTime"))) {
            criteria.andLessThanOrEqualTo("createTime", searchMap.get("endTime"));
        }
        // 1.2 查询条件：描述
        if (StringUtil.isNotEmpty((String) searchMap.get("name"))) {
            criteria.andLike("carNumber", "%"+searchMap.get("name")+"%");
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
        return (Page<Car>) carMapper.selectByExample(example);
    }

    /**
     * 添加车辆对象
     */
    @Override
    public void addCar(Car Car) {
        carMapper.insertSelective(Car);
    }

    /**
     * 修改车辆对象
     */
    @Override
    public void updateCar(Car Car) {
        carMapper.updateByPrimaryKeySelective(Car);
    }

    /**
     * 根据 id 查询车辆对象
     */
    @Override
    public Car getCarById(Integer id) {
        return carMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除车辆信息
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        // E.检查数据是否有效
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(CommunityConstant.DELETE_IDS_IS_NULL);
        }
        for (Integer id : ids) {
            carMapper.deleteByPrimaryKey(id);
        }
    }
}
