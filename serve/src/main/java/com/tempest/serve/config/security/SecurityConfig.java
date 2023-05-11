package com.tempest.serve.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.server.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    @Value("${serve.controller.api-prefix:}")
    private String apiPrefix = "/api";
    @Value("${serve.controller.open-prefix:}")
    private String openPrefix = "/open";
    @Value("${serve.controller.logout-url:}")
    private String logoutUrl = "/api/login";
    @Value("${serve.controller.login-url:}")
    private String loginUrl = "/api/logout";
    @Value("${serve.controller.cors-all}")
    private boolean corsAll = false;

    public SecurityConfig(RedisTemplate<String, Object> redisTemplate,
                          UserDetailsService userDetailsService,
                          ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(apiPrefix + "/**")
                        .authenticated()
                        .requestMatchers("/**", logoutUrl, loginUrl, openPrefix + "/**")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(logoutUrl)
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry())
                )
                .csrf(csrf -> csrf
                                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                        // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .cors(cors -> cors.configurationSource(configurationSource()))
                .addFilterAt(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 设置所有接口允许跨域
     * 如果配置文件中没有设置允许跨域，则返回null
     *
     * @return configurationSource
     */
    @Bean
    public CorsConfigurationSource configurationSource() {
        if (!corsAll) {
            return null;
        }

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许所有跨域，和预请求跨域
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 更改cookieSerializer，增加samesite=none;secure
     * 如果没有配置serve.controller.cors-all，或设置为false，则不设置
     *
     * @return cookieSerializer
     */
    @Bean
    @ConditionalOnProperty(value = "serve.controller.cors-all", havingValue = "true")
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // 允许跨域访问cookie
        serializer.setSameSite(Cookie.SameSite.NONE.attributeValue());
        serializer.setUseSecureCookie(true);
        return serializer;
    }

    /**
     * 应用spring session
     *
     * @return sessionRegistry
     */
    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(new RedisIndexedSessionRepository(redisTemplate));
    }

    /**
     * 应用spring session的rememberMeServices
     *
     * @return rememberMeServices
     */
    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        return new SpringSessionRememberMeServices();
    }

    /**
     * 返回自定义usernamePasswordAuthenticationFilter，使用json登陆
     * 设置了userDetailsService、DaoAuthenticationProvider、passwordEncoder
     *
     * @return usernamePasswordAuthenticationFilter
     */
    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
        JsonUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter =
                new JsonUsernamePasswordAuthenticationFilter(objectMapper);

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);

        ProviderManager providerManager = new ProviderManager(provider);
        usernamePasswordAuthenticationFilter.setAuthenticationManager(providerManager);

        SpringSessionRememberMeServices rememberMeServices = rememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        usernamePasswordAuthenticationFilter.setRememberMeServices(rememberMeServices);

        // 将context保存到rides
        usernamePasswordAuthenticationFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        usernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(
                new RegisterSessionAuthenticationStrategy(sessionRegistry())
        );
        return usernamePasswordAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
