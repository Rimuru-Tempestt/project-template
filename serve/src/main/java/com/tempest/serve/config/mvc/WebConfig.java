package com.tempest.serve.config.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 为不同包下的controller添加前缀
 *
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "serve.controller")
@Setter
public class WebConfig implements WebMvcConfigurer {
    private PrefixConfig[] prefixConfigs;

    @Override
    public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        for (PrefixConfig config : prefixConfigs) {
            String prefix = config.getPrefix();
            String packageName = config.getPackageName();
            configurer.addPathPrefix(prefix, c-> c.getPackageName().startsWith(packageName));
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrefixConfig {
        private String prefix;
        private String packageName;
    }
}
