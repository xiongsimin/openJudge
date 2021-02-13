package com.openJudge.openJudge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author aries
 * @Data 2021-02-08
 */
@Configuration
@ConfigurationProperties(prefix = "kim.aries")
@PropertySource(value = {"classpath:config.properties"})
public class ConfigProperties {
    private static String jdkPath;
    private static String tempFilePath;
    private static String uploadPath;

    public static String getJdkPath() {
        return jdkPath;
    }

    public static String getTempFilePath() {
        return tempFilePath;
    }

    public static String getUploadPath() {
        return uploadPath;
    }

    public static void setJdkPath(String jdkPath) {
        ConfigProperties.jdkPath = jdkPath;
    }

    public static void setTempFilePath(String tempFilePath) {
        ConfigProperties.tempFilePath = tempFilePath;
    }

    public static void setUploadPath(String uploadPath) {
        ConfigProperties.uploadPath = uploadPath;
    }
}
