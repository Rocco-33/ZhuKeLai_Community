package com.jason.community.mvc.mapper;

import java.util.List;

import com.jason.community.entity.Role;
import com.jason.community.entity.RoleExample;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {

    List<Role> selectAssignedRole(Integer adminId);

    List<Role> selectUnAssignedRole(Integer adminId);

    int countByExample(RoleExample example);

    void deleteAdminRel(@Param("adminId") Integer adminId, @Param("roleId") Integer roleId);

    void deleteAuthRel(Integer roleId);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    void insertAdminRel(@Param("adminId") Integer adminId, @Param("roleId") Integer roleId);

    void insertAuthRel(@Param("roleId") Integer roleId,@Param("authIdList") List<Integer> authIdList);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}