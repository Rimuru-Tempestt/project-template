package com.tempest.serve.manager.impl;

import com.tempest.serve.manager.UserInfoManager;
import com.tempest.serve.mapper.UserMapper;
import com.tempest.serve.mapper.UserRoleMapper;
import com.tempest.serve.pojo.dto.UserInfoDTO;
import com.tempest.serve.pojo.entity.Role;
import com.tempest.serve.pojo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Component
@Slf4j
public class UserInfoManagerImpl implements UserInfoManager {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    public UserInfoManagerImpl(UserMapper userMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public UserInfoDTO getUserInfoByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            log.error("不存在的用户，用户名为{}", username);
            throw new  UsernameNotFoundException("用户" + username +"不存在");
        }
        List<Role> roles = userRoleMapper.selectRolesByUserId(user.getId());
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfoDTO);
        userInfoDTO.setRoles(roles);
        return userInfoDTO;
    }
}
