package com.jason.community.mvc.mapper;

import java.util.List;

import com.jason.community.entity.Auth;
import com.jason.community.entity.AuthExample;
import org.apache.ibatis.annotations.Param;

public interface AuthMapper {

    int countByExample(AuthExample example);

    void deleteRoleRel(@Param("roleId") Integer roleId, @Param("authId") Integer authId);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    void insertRoleRel(@Param("roleId") Integer roleId, @Param("authId") Integer authId);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectAssignedAuth(Integer roleId);

    List<Auth> selectUnAssignedAuth(Integer roleId);

    List<Integer> selectAssignedAuthIdByRoleId(Integer roleId);

    List<String> selectAssignedAuthNameByAdminId(Integer adminId);

    List<Auth> selectAllAuth();

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);

}