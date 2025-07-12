package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Repair;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface RepairService {

    Page<Repair> getAllRepairBySearch(Map<String, Object> searchMap);

    void addRepair(Repair repair);

    void updateRepair(Repair repair);

    Repair getRepairById(Integer id);

    void deleteByIds(List<Integer> ids);
}
