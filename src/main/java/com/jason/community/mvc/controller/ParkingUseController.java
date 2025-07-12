package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.CommunityUtil;
import com.jason.community.common.CommunityConstant;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.config.OSSProperties;
import com.jason.community.entity.ParkingUse;
import com.jason.community.mvc.service.api.ParkingUseService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
@PreAuthorize("hasAuthority('parking:write')")
@RestController
@RequestMapping("/parkinguse")
public class ParkingUseController {

    @Resource
    private ParkingUseService parkingUseService;

    @Resource // 装配 OSS 服务接口配置类
    private OSSProperties ossProperties;

    /**
     * 获取所有查询到的车位使用信息
     */
    @PreAuthorize("hasAuthority('parking:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<ParkingUse>> getAllParkingUseBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<ParkingUse> parkingUsePage = parkingUseService.getAllParkingUseBySearch(searchMap);
            return PageResultEntity.successWithData(parkingUsePage, parkingUsePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 上传车位使用图片到 OSS服务器
     */
    @RequestMapping("/upload/picture")
    public ResultEntity<String> uploadPicture(@RequestParam("file") MultipartFile parkingUsePicture, HttpServletRequest request) throws IOException {

        // E.检查 文件是否选中
        if (!ServletFileUpload.isMultipartContent(request)) {
            return ResultEntity.failed(CommunityConstant.FILE_NO_SELECTED);
        }
        // 1.上传车位使用图片到 OSS服务器
        ResultEntity<String> uploadParkingUsePicResultEntity =
                CommunityUtil.uploadFileToOss(
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        ossProperties.getEndPoint(),
                        parkingUsePicture.getOriginalFilename(),
                        parkingUsePicture.getInputStream()
                );
        // E.检查 车位使用图片是否上传成功
        if (!uploadParkingUsePicResultEntity.getResult()) {
            return uploadParkingUsePicResultEntity;
        }
        // 2.获取车位使用图片的访问路径，并返回
        String ParkingUsePicturePath = uploadParkingUsePicResultEntity.getData();
        return ResultEntity.successWithData(ParkingUsePicturePath);
    }

    /**
     * 从 OSS服务器 删除车位使用图片
     */
    @RequestMapping("/delete/picture")
    public ResultEntity<String> deletePicture(@RequestParam String parkingUsePicture) {

        // 1.从图片访问路径 获取文件存储位置（目录+文件名）
        String objectName = parkingUsePicture.substring(parkingUsePicture.indexOf("/")+1);

        // 2.从 OSS服务器 删除车位使用图片
        return CommunityUtil.deleteFileFromOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName(),
                objectName);
    }

    /**
     * 添加车位使用信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addParkingUse(@RequestBody ParkingUse parkingUse) {
        try {
            // 执行添加业务
            parkingUseService.addParkingUse(parkingUse);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改车位使用信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateParkingUse(@RequestBody ParkingUse parkingUse) {
        try {
            // 执行修改业务
            parkingUseService.updateParkingUse(parkingUse);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取车位使用信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<ParkingUse> getParkingUseById(Integer id) {
        try {
            // 执行查询业务
            ParkingUse parkingUse = parkingUseService.getParkingUseById(id);
            return ResultEntity.successWithData(parkingUse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改车位使用状态
     */
    @RequestMapping("/update/status/{status}/{id}")
    public ResultEntity<String> updateStatus(
            @PathVariable("id") Integer id,
            @PathVariable("status") String status
    ) {
        try {
            // 执行修改业务
            parkingUseService.updateStatus(id, status);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除车位使用信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteCommunities(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            parkingUseService.deleteCommunities(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
