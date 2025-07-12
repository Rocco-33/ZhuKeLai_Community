package com.jason.community.mvc.controller;

import com.github.pagehelper.Page;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.entity.Letter;
import com.jason.community.mvc.service.api.LetterService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('service:write')")
@RestController
@RequestMapping("/letter")
public class LetterController {

    @Resource
    private LetterService letterService;

    /**
     * 获取所有查询到的信箱信息
     */
    @PreAuthorize("hasAuthority('service:read')")
    @RequestMapping("/get/all/by/search")
    public PageResultEntity<Page<Letter>> getAllLetterBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Letter> letterPage = letterService.getAllLetterBySearch(searchMap);
            return PageResultEntity.successWithData(letterPage, letterPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加信箱信息
     */
    @RequestMapping("/add")
    public ResultEntity<String> addLetter(@RequestBody Letter letter) {
        try {
            // 执行添加业务
            letterService.addLetter(letter);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改信箱信息
     */
    @RequestMapping("/update")
    public ResultEntity<String> updateLetter(@RequestBody Letter letter) {
        try {
            // 执行修改业务
            letterService.updateLetter(letter);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取信箱信息
     */
    @RequestMapping("/get/by/id")
    public ResultEntity<Letter> getLetterById(Integer id) {
        try {
            // 执行查询业务
            Letter letter = letterService.getLetterById(id);
            return ResultEntity.successWithData(letter);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除信箱信息
     */
    @RequestMapping("/delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            letterService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
