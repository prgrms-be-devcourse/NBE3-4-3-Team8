package com.ll.nbe342team8.global.config;

import lombok.Getter;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

    private static Environment environment;
    @Autowired
    public void setEnvironment(Environment environment) {
        AppConfig.environment = environment;
    }
    public static boolean isProd() {
        return environment.matchesProfiles("prod");
    }
    public static boolean isDev() {
        return environment.matchesProfiles("dev");
    }
    public static boolean isTest() {
        return environment.matchesProfiles("test");
    }
    public static boolean isNotProd() {
        return !isProd();
    }

    @Getter
    private static Tika tika;

    @Autowired
    public void setTika(Tika tika) {
        AppConfig.tika = tika;
    }



    @Getter
    public static String genFileDirPath;

    @Value("${custom.genFile.dirPath}")
    public void setGenFileDirPath(String genFileDirPath) {
        this.genFileDirPath = genFileDirPath;
    }

    public static String getTempDirPath() {
        return System.getProperty("java.io.tmpdir");
    }

}
