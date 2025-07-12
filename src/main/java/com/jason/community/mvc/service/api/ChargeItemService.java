package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.ChargeItem;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface ChargeItemService {

    Page<ChargeItem> getAllChargeItemBySearch(Map<String, Object> searchMap);

    void addChargeItem(ChargeItem chargeItem);

    void updateChargeItem(ChargeItem chargeItem);

    ChargeItem getChargeItemById(Integer id);

    void deleteByIds(List<Integer> ids);
}
