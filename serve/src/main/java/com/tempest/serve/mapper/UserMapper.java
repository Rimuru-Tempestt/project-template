package com.tempest.serve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tempest.serve.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author RimuruTempest
 * @since 2023/5/09 14:59
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 用户类
     */
    User selectByUsername(String username);
}

