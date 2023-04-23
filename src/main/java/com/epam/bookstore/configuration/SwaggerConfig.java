package com.epam.bookstore.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class SwaggerConfig {

    private static final String ANY_ENDPOINT_WILDCARD = "*";

    @Value("${spring.data.rest.basePath}")
    private String apiVersion;

    @Value("${server.servlet.context-path}")
    private String applicationContext;

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant(applicationContext.concat(apiVersion).concat(ANY_ENDPOINT_WILDCARD)))
                .build();
    }
}
