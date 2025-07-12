package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Asset;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface AssetService {

    Page<Asset> getAllAssetBySearch(Map<String, Object> searchMap);

    void addAsset(Asset asset);

    void updateAsset(Asset asset);

    Asset getAssetById(Integer id);

    void deleteByIds(List<Integer> ids);
}
