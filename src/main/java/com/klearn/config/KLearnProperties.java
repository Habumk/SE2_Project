package com.klearn.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "klearn")
@Getter
@Setter
public class KLearnProperties {

    private JwtProperties jwt = new JwtProperties();
    private SecurityProperties security = new SecurityProperties();
    private XpProperties xp = new XpProperties();
    private StreakProperties streak = new StreakProperties();

    @Getter
    @Setter
    public static class JwtProperties {
        private String secret;
        private long expiration;
    }

    @Getter
    @Setter
    public static class SecurityProperties {
        private boolean csrfEnabled;
        private CorsProperties cors = new CorsProperties();
    }

    @Getter
    @Setter
    public static class CorsProperties {
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;
    }

    @Getter
    @Setter
    public static class XpProperties {
        private int lessonReward;
        private LevelThresholds levelThresholds = new LevelThresholds();
    }

    @Getter
    @Setter
    public static class LevelThresholds {
        private int level1;
        private int level2;
        private int level3;
        private int level4;
        private int level5;
    }

    @Getter
    @Setter
    public static class StreakProperties {
        private int maxDays;
        private boolean resetOnSkip;
    }
}
