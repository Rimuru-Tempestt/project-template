package com.tempest.serve.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tempest.serve.pojo.vo.LoginInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Slf4j
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter implements InitializingBean {
    private final ObjectMapper objectMapper;
    private final ThreadLocal<LoginInfo> loginInfo;
    @Value("${serve.controller.login-url}")
    private String defaultLoginUrl;

    {
        loginInfo = new ThreadLocal<>();
    }

    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() {
        this.setFilterProcessesUrl(defaultLoginUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            return super.attemptAuthentication(request, response);
        } finally {
            loginInfo.remove();
        }
    }

    protected LoginInfo getLoginInfo(HttpServletRequest request) {
        LoginInfo info = loginInfo.get();
        if (info != null) {
            return loginInfo.get();
        }

        try {
            info = objectMapper.readValue(request.getInputStream(), LoginInfo.class);
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.debug("初始化用户信息错误");
            }
            throw new AuthenticationServiceException("初始化用户信息错误");
        }
        loginInfo.set(info);
        return info;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return getLoginInfo(request).getUsername();
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return getLoginInfo(request).getPassword();
    }

    public String getDefaultLoginUrl() {
        return defaultLoginUrl;
    }

    public void setDefaultLoginUrl(String defaultLoginUrl) {
        this.defaultLoginUrl = defaultLoginUrl;
    }
}
