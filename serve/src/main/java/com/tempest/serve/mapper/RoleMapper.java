package com.tempest.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tempest.serve.pojo.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Role)表数据库访问层
 *
 * @author RimuruTempest
 * @since 2023/5/09 15:03
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}

