package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.CommunityUtil;
import com.jason.community.common.CommunityConstant;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.config.OSSProperties;
import com.jason.community.entity.Pet;
import com.jason.community.mvc.service.api.PetService;
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
@RequestMapping("/pet")
public class PetController {

    @Resource
    private PetService petService;

    @Resource // 装配 OSS 服务接口配置类
    private OSSProperties ossProperties;

    /**
     * 获取所有查询到的宠物信息
     */
    @PreAuthorize("hasAuthority('owner:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Pet>> getAllPetBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Pet> PetPage = petService.getAllPetBySearch(searchMap);
            return PageResultEntity.successWithData(PetPage, PetPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 上传宠物图片到 OSS服务器
     */
    @RequestMapping("/upload/picture")
    public ResultEntity<String> uploadPicture(@RequestParam("file") MultipartFile PetPicture, HttpServletRequest request) throws IOException {

        // E.检查 文件是否选中
        if (!ServletFileUpload.isMultipartContent(request)) {
            return ResultEntity.failed(CommunityConstant.FILE_NO_SELECTED);
        }
        // 1.上传宠物图片到 OSS服务器
        ResultEntity<String> uploadPetPicResultEntity =
                CommunityUtil.uploadFileToOss(
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        ossProperties.getEndPoint(),
                        PetPicture.getOriginalFilename(),
                        PetPicture.getInputStream()
                );
        // E.检查 宠物图片是否上传成功
        if (!uploadPetPicResultEntity.getResult()) {
            return uploadPetPicResultEntity;
        }
        // 2.获取宠物图片的访问路径，并返回
        String PetPicturePath = uploadPetPicResultEntity.getData();
        return ResultEntity.successWithData(PetPicturePath);
    }

    /**
     * 从 OSS服务器 删除宠物图片
     */
    @RequestMapping("/delete/picture")
    public ResultEntity<String> deletePicture(@RequestParam String petPicture) {

        // 1.从图片访问路径 获取文件存储位置（目录+文件名）
        String objectName = petPicture.substring(petPicture.indexOf("/")+1);

        // 2.从 OSS服务器 删除宠物图片
        return CommunityUtil.deleteFileFromOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName(),
                objectName);
    }

    /**
     * 添加宠物信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addPet(@RequestBody Pet pet) {
        try {
            // 执行添加业务
            petService.addPet(pet);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改宠物信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updatePet(@RequestBody Pet pet) {
        try {
            // 执行修改业务
            petService.updatePet(pet);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取宠物信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<Pet> getPetById(Integer id) {
        try {
            // 执行查询业务
            Pet pet = petService.getPetById(id);
            return ResultEntity.successWithData(pet);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除宠物信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            petService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
