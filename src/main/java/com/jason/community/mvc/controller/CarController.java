package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.CommunityUtil;
import com.jason.community.common.CommunityConstant;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.config.OSSProperties;
import com.jason.community.entity.Car;
import com.jason.community.mvc.service.api.CarService;
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
@RequestMapping("/car")
public class CarController {

    @Resource
    private CarService carService;

    @Resource // 装配 OSS 服务接口配置类
    private OSSProperties ossProperties;

    /**
     * 获取所有查询到的车辆信息
     */
    @PreAuthorize("hasAuthority('owner:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Car>> getAllCarBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Car> carPage = carService.getAllCarBySearch(searchMap);
            return PageResultEntity.successWithData(carPage, carPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 上传车辆图片到 OSS服务器
     */
    @RequestMapping("/upload/picture")
    public ResultEntity<String> uploadPicture(@RequestParam("file") MultipartFile CarPicture, HttpServletRequest request) throws IOException {

        // E.检查 文件是否选中
        if (!ServletFileUpload.isMultipartContent(request)) {
            return ResultEntity.failed(CommunityConstant.FILE_NO_SELECTED);
        }
        // 1.上传车辆图片到 OSS服务器
        ResultEntity<String> uploadCarPicResultEntity =
                CommunityUtil.uploadFileToOss(
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        ossProperties.getEndPoint(),
                        CarPicture.getOriginalFilename(),
                        CarPicture.getInputStream()
                );
        // E.检查 车辆图片是否上传成功
        if (!uploadCarPicResultEntity.getResult()) {
            return uploadCarPicResultEntity;
        }
        // 2.获取车辆图片的访问路径，并返回
        String CarPicturePath = uploadCarPicResultEntity.getData();
        return ResultEntity.successWithData(CarPicturePath);
    }

    /**
     * 从 OSS服务器 删除车辆图片
     */
    @RequestMapping("/delete/picture")
    public ResultEntity<String> deletePicture(@RequestParam String carPicture) {

        // 1.从图片访问路径 获取文件存储位置（目录+文件名）
        String objectName = carPicture.substring(carPicture.indexOf("/")+1);

        // 2.从 OSS服务器 删除车辆图片
        return CommunityUtil.deleteFileFromOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                ossProperties.getBucketName(),
                objectName);
    }

    /**
     * 添加车辆信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addCar(@RequestBody Car car) {
        try {
            // 执行添加业务
            carService.addCar(car);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改车辆信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateCar(@RequestBody Car car) {
        try {
            // 执行修改业务
            carService.updateCar(car);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取车辆信息
     */
    @RequestMapping("get/by/id")
    public ResultEntity<Car> getCarById(Integer id) {
        try {
            // 执行查询业务
            Car car = carService.getCarById(id);
            return ResultEntity.successWithData(car);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除车辆信息
     */
    @RequestMapping("delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            carService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
