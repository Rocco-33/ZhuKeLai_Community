package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.ChargeItem;
import com.jason.community.mvc.service.api.ChargeItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('charge:write')")
@RestController
@RequestMapping("/chargeitem")
public class ChargeItemController {

    @Resource
    private ChargeItemService chargeItemService;

    /**
     * 获取所有查询到的收费账单信息
     */
    @PreAuthorize("hasAuthority('charge:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<ChargeItem>> getAllChargeItemBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<ChargeItem> chargeItemPage = chargeItemService.getAllChargeItemBySearch(searchMap);
            return PageResultEntity.successWithData(chargeItemPage, chargeItemPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加收费账单信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addChargeItem(@RequestBody ChargeItem chargeItem) {
        try {
            // 执行添加业务
            chargeItemService.addChargeItem(chargeItem);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改收费账单信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateChargeItem(@RequestBody ChargeItem chargeItem) {
        try {
            // 执行修改业务
            chargeItemService.updateChargeItem(chargeItem);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取收费账单信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<ChargeItem> getChargeItemById(Integer id) {
        try {
            // 执行查询业务
            ChargeItem chargeItem = chargeItemService.getChargeItemById(id);
            return ResultEntity.successWithData(chargeItem);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除收费账单信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            chargeItemService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
