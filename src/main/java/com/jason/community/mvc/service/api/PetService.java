package com.jason.community.mvc.service.api;

import com.github.pagehelper.Page;
import com.jason.community.entity.Pet;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @version 1.0
 */
public interface PetService {

    Page<Pet> getAllPetBySearch(Map<String, Object> searchMap);

    void addPet(Pet pet);

    void updatePet(Pet pet);

    Pet getPetById(Integer id);

    void deleteByIds(List<Integer> ids);
}
