package com.tempest.serve.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                )
                .cors(Customizer.withDefaults())
                .addFilterAt(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(new RedisIndexedSessionRepository(redisTemplate));
    }

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

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        return new SpringSessionRememberMeServices();
    }
}
