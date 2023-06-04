package org.stupid_talking_potatoes.kis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "tago")
@Data
@Component
public class TagoServiceConfig {
    private String serviceKey;
    private Integer cityCode;
}
