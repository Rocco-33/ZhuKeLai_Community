package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Car;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface CarService {

    Page<Car> getAllCarBySearch(Map<String, Object> searchMap);

    void addCar(Car car);

    void updateCar(Car car);

    Car getCarById(Integer id);

    void deleteByIds(List<Integer> ids);
}
