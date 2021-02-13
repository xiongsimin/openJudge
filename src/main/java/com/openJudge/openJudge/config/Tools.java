package com.openJudge.openJudge.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Author aries
 * @Data 2021-02-08
 */
@EnableConfigurationProperties
public class Tools {
    public static final String jdkPath=ConfigProperties.getJdkPath();
}
