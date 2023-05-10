package com.tempest.serve.manager;

import com.tempest.serve.pojo.dto.UserInfoDTO;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
public interface UserInfoManager {
    /**
     * 获取userInfoDto
     *
     * @param username 用户名
     * @return userInfoDto
     */
    UserInfoDTO getUserInfoByUsername(String username);
}
