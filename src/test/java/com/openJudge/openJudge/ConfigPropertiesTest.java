package com.openJudge.openJudge;

import com.openJudge.openJudge.config.ConfigProperties;
import com.openJudge.openJudge.config.Tools;
import org.junit.Test;

/**
 * @Author aries
 * @Data 2021-02-08
 */
public class ConfigPropertiesTest {
    @Test
    public void testPath(){
        System.out.println(Tools.jdkPath);
        System.out.println(ConfigProperties.getTempFilePath());
        System.out.println(ConfigProperties.getUploadPath());
    }
}
