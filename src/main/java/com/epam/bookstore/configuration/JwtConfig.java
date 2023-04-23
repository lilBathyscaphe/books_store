package com.epam.bookstore.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "application.jwt")
@Data
@NoArgsConstructor
public class JwtConfig {

    private String secretKey;
    private Integer tokenExpirationAfterDays;
    private String tokenName;

}
