package com.jason.community.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jason.community.common.CommunityConstant;
import com.jason.community.entity.ChargeItem;
import com.jason.community.mvc.mapper.ChargeItemMapper;
import com.jason.community.mvc.service.api.ChargeItemService;
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
public class ChargeItemServiceImpl implements ChargeItemService {
    
    @Resource
    private ChargeItemMapper chargeItemMapper;

    /**
     * 获取查询的收费账单对象
     */
    @Override
    public Page<ChargeItem> getAllChargeItemBySearch(Map<String, Object> searchMap) {

        // E.检查 数据是否有效
        if (searchMap == null) {
            throw new RuntimeException(CommunityConstant.SEARCH_MAP_IS_NULL);
        }
        // 1.初始化查询条件
        int pageNum = 1;
        int pageSize = 2;
        Example example = new Example(ChargeItem.class);
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
            criteria.andLike("name", "%"+searchMap.get("name")+"%");
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
        return (Page<ChargeItem>) chargeItemMapper.selectByExample(example);
    }

    /**
     * 添加收费账单对象
     */
    @Override
    public void addChargeItem(ChargeItem ChargeItem) {
        chargeItemMapper.insertSelective(ChargeItem);
    }

    /**
     * 修改收费账单对象
     */
    @Override
    public void updateChargeItem(ChargeItem ChargeItem) {
        chargeItemMapper.updateByPrimaryKeySelective(ChargeItem);
    }

    /**
     * 根据 id 查询收费账单对象
     */
    @Override
    public ChargeItem getChargeItemById(Integer id) {
        return chargeItemMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除收费账单信息
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        // E.检查数据是否有效
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(CommunityConstant.DELETE_IDS_IS_NULL);
        }
        for (Integer id : ids) {
            chargeItemMapper.deleteByPrimaryKey(id);
        }
    }
    
}
