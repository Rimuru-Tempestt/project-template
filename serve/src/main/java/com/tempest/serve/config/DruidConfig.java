package com.tempest.serve.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Configuration
public class DruidConfig {
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSourceWrapper();
    }
}
