package com.tempest.serve.config.security;

import com.tempest.serve.manager.UserInfoManager;
import com.tempest.serve.pojo.dto.UserInfoDTO;
import com.tempest.serve.pojo.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Component
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserInfoManager userInfoManager;

    public CustomUserDetailsServiceImpl(UserInfoManager userInfoManager) {
        this.userInfoManager = userInfoManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoDTO userinfo = userInfoManager.getUserInfoByUsername(username);
        String[] roles = userinfo.getRoles().stream().map(Role::getRoleName).toArray(String[]::new);

        if (log.isDebugEnabled()) {
            log.debug("获取用户成功，用户为{}，权限有{}", username, Arrays.toString(roles));
        }

        return User.withUsername(userinfo.getUsername())
                .password(userinfo.getPassword())
                .roles(roles)
                .build();
    }
}
