package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Letter;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface LetterService {

    Page<Letter> getAllLetterBySearch(Map<String, Object> searchMap);

    void addLetter(Letter letter);

    void updateLetter(Letter letter);

    Letter getLetterById(Integer id);

    void deleteByIds(List<Integer> ids);
}
