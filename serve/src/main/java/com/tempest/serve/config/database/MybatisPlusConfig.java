package com.tempest.serve.config.database;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rimuru Tempest
 * @version 1.0.0
 */
@Configuration
@MapperScan("com.tempest.serve.mapper")
public class MybatisPlusConfig {
}
