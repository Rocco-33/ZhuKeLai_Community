package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.CommunityUtil;
import com.jason.community.common.CommunityConstant;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.config.OSSProperties;
import com.jason.community.entity.Owner;
import com.jason.community.mvc.service.api.OwnerService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('owner:write')")
@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Resource
    private OwnerService ownerService;

    @Resource // 装配 OSS 服务接口配置类
    private OSSProperties ossProperties;

    /**
     * 获取所有查询到的人员信息
     */
    @PreAuthorize("hasAuthority('owner:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Owner>> getAllOwnerBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Owner> OwnerPage = ownerService.getAllOwnerBySearch(searchMap);
            return PageResultEntity.successWithData(OwnerPage, OwnerPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 上传人员图片到 OSS服务器
     */
    @RequestMapping("/upload/picture")
    public ResultEntity<String> uploadPicture(@RequestParam("file") MultipartFile ownerPicture, HttpServletRequest request) throws IOException {

        // E.检查 文件是否选中
        if (!ServletFileUpload.isMultipartContent(request)) {
            return ResultEntity.failed(CommunityConstant.FILE_NO_SELECTED);
        }
        // 1.上传人员图片到 OSS服务器
        ResultEntity<String> uploadOwnerPicResultEntity =
                CommunityUtil.uploadFileToOss(
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        ossProperties.getEndPoint(),
                        ownerPicture.getOriginalFilename(),
                        ownerPicture.getInputStream()
                );
        // E.检查 人员图片是否上传成功
        if (!uploadOwnerPicResultEntity.getResult()) {
            return uploadOwnerPicResultEntity;
        }
        // 2.获取人员图片的访问路径，并返回
        String ownerPicturePath = uploadOwnerPicResultEntity.getData();
        return ResultEntity.successWithData(ownerPicturePath);
    }

    /**
     * 从 OSS服务器 删除人员图片
     */
    @RequestMapping("/delete/picture")
    public ResultEntity<String> deletePicture(@RequestParam String ownerPicture) {

        // 1.从图片访问路径 获取文件存储位置（目录+文件名）
        String objectName = ownerPicture.substring(ownerPicture.indexOf("/")+1);

        // 2.从 OSS服务器 删除人员图片
        return CommunityUtil.deleteFileFromOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName(),
                objectName);
    }

    /**
     * 添加人员信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addOwner(@RequestBody Owner owner) {
        try {
            // 执行添加业务
            ownerService.addOwner(owner);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改人员信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateOwner(@RequestBody Owner owner) {
        try {
            // 执行修改业务
            ownerService.updateOwner(owner);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取人员信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<Owner> getOwnerById(Integer id) {
        try {
            // 执行查询业务
            Owner owner = ownerService.getOwnerById(id);
            return ResultEntity.successWithData(owner);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除人员信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            ownerService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
