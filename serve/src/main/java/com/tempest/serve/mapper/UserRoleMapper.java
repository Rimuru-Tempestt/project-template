package com.tempest.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tempest.serve.pojo.entity.Role;
import com.tempest.serve.pojo.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (UserRole)表数据库访问层
 *
 * @author RimuruTempest
 * @since 2023/5/09 15:04
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /**
     * 通过userId查询所有的role
     * @param id 用户id
     * @return role列表
     */
    List<Role> selectRolesByUserId(Long id);
}

