package com.tempest.serve.manager.impl;

import com.tempest.serve.manager.UserInfoManager;
import com.tempest.serve.pojo.dto.UserInfoDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@SpringBootTest
class UserInfoManagerImplTest {
    private final UserInfoManager userInfoManager;

    @Autowired
    public UserInfoManagerImplTest(UserInfoManager userInfoManager) {
        this.userInfoManager = userInfoManager;
    }

    @Test
    void getUserInfoById() {
        UserInfoDTO userInfo = userInfoManager.getUserInfoByUsername("rimuru");
        Assertions.assertThat(userInfo.getUsername()).isNotNull();
        Assertions.assertThat(userInfo.getPassword()).isNotNull();
    }
}