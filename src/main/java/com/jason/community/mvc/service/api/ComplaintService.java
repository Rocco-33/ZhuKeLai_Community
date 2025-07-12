package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Complaint;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface ComplaintService {

    Page<Complaint> getAllComplaintBySearch(Map<String, Object> searchMap);

    void addComplaint(Complaint complaint);

    void updateComplaint(Complaint complaint);

    Complaint getComplaintById(Integer id);

    void deleteByIds(List<Integer> ids);
}
