package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Owner;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface OwnerService {

    Page<Owner> getAllOwnerBySearch(Map<String, Object> searchMap);

    void addOwner(Owner owner);

    void updateOwner(Owner owner);

    Owner getOwnerById(Integer id);

    void deleteByIds(List<Integer> ids);
}
