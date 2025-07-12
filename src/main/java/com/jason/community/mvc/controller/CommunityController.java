package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.CommunityUtil;
import com.jason.community.common.CommunityConstant;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.config.OSSProperties;
import com.jason.community.entity.Community;
import com.jason.community.mvc.service.api.CommunityService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('community:write')")
@RestController
@RequestMapping("/community")
public class CommunityController {

    @Resource
    private CommunityService communityService;

    @Resource // 装配 OSS 服务接口配置类
    private OSSProperties ossProperties;

    /**
     * 获取所有查询到的小区信息
     */
    @PreAuthorize("hasAuthority('community:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Community>> getAllCommunityBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Community> communityPage = communityService.getAllCommunityBySearch(searchMap);
            return PageResultEntity.successWithData(communityPage, communityPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 上传小区图片到 OSS服务器
     */
    @RequestMapping("/upload/picture")
    public ResultEntity<String> uploadPicture(@RequestParam("file") MultipartFile communityPicture, HttpServletRequest request) throws IOException {

        // E.检查 文件是否选中
        if (!ServletFileUpload.isMultipartContent(request)) {
            return ResultEntity.failed(CommunityConstant.FILE_NO_SELECTED);
        }
        // 1.上传小区图片到 OSS服务器
        ResultEntity<String> uploadCommunityPicResultEntity =
                CommunityUtil.uploadFileToOss(
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        ossProperties.getEndPoint(),
                        communityPicture.getOriginalFilename(),
                        communityPicture.getInputStream()
                );
        // E.检查 小区图片是否上传成功
        if (!uploadCommunityPicResultEntity.getResult()) {
            return uploadCommunityPicResultEntity;
        }
        // 2.获取小区图片的访问路径，并返回
        String communityPicturePath = uploadCommunityPicResultEntity.getData();
        return ResultEntity.successWithData(communityPicturePath);
    }

    /**
     * 从 OSS服务器 删除小区图片
     */
    @RequestMapping("/delete/picture")
    public ResultEntity<String> deletePicture(@RequestParam String communityPicture) {

        // 1.从图片访问路径 获取文件存储位置（目录+文件名）
        String objectName = communityPicture.substring(communityPicture.indexOf("/")+1);

        // 2.从 OSS服务器 删除小区图片
        return CommunityUtil.deleteFileFromOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName(),
                objectName);
    }

    /**
     * 添加小区信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addCommunity(@RequestBody Community community) {
        try {
            // 执行添加业务
            communityService.addCommunity(community);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改小区信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateCommunity(@RequestBody Community community) {
        try {
            // 执行修改业务
            communityService.updateCommunity(community);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取小区信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<Community> getCommunityById(Integer id) {
        try {
            // 执行查询业务
            Community community = communityService.getCommunityById(id);
            return ResultEntity.successWithData(community);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改小区状态
     */
    @RequestMapping("/update/status/{status}/{id}")
    public ResultEntity<String> updateStatus(
            @PathVariable("id") Integer id,
            @PathVariable("status") String status
    ) {
        try {
            // 执行修改业务
            communityService.updateStatus(id, status);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除小区信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteCommunities(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            communityService.deleteCommunities(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
