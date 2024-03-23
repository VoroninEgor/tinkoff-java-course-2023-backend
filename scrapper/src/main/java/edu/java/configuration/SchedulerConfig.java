package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SchedulerConfig {
    @Bean
    public Long schedulerIntervalMs(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }

    @Bean
    @Primary
    public Long schedulerForceCheckDelay(ApplicationConfig config) {
        return config.scheduler().forceCheckDelay().toMinutes();
    }
}
