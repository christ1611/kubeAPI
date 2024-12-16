package com.kubeApi.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

@Slf4j
@Configuration
@OpenAPIDefinition(info =
@Info(title = "kubeApi", version = "v0.0.1", description = "kubeApi",
        license = @License(name = "Apache 2.0", url = "http://springdoc.org")))
public class SwaggerConfig {

    public static final String DATE_FORMAT = "uuuu/MM/dd";
    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder().parseStrict()
            .appendPattern(DATE_FORMAT)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

    public static final String DATETIME_FORMAT = "uuuu/MM/dd HH:mm:ss";
    public static final DateTimeFormatter DATETIME_FORMATTER = new DateTimeFormatterBuilder().parseStrict()
            .appendPattern(DATETIME_FORMAT)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);
    public static final String JWT_TOKEN_HEADER_PARAM         = "X-Auth-Token";

    public SwaggerConfig() {
        log.debug("SwaggerConfig - Schema");
        Schema<LocalDate> shld = new Schema<>();
        shld.example(LocalDate.now().format(DATE_FORMATTER));
        Schema<LocalDateTime> shldt = new Schema<>();
        shldt.example(LocalDateTime.now().format(DATETIME_FORMATTER));

        Schema<ZonedDateTime> shzldt = new Schema<>();
        shzldt.example(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

        SpringDocUtils.getConfig().replaceWithSchema(LocalDate.class, shld);
        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class, shldt);
        SpringDocUtils.getConfig().replaceWithSchema(ZonedDateTime.class, shzldt);
    }
    @Bean
    public GroupedOpenApi k8s() {
        log.debug("GroupedOpenApi-auth");

        return GroupedOpenApi.builder()
                .group("01.k8s")
                .packagesToScan("com.kubeApi.server.k8s")
                .addOpenApiCustomizer(buildSecurityOpenApi())
                .build();
    }
    @Bean
    public GroupedOpenApi podman() {
        log.debug("GroupedOpenApi-auth");

        return GroupedOpenApi.builder()
                .group("02.podman")
                .packagesToScan("com.kubeApi.server.podman")
                .build();
    }
    @Bean
    public GroupedOpenApi quartz() {
        log.debug("GroupedOpenApi-auth");

        return GroupedOpenApi.builder()
                .group("03.quartz")
                .packagesToScan("com.kubeApi.core.quartz")
                .build();
    }
    @Bean
    public GroupedOpenApi common() {
        log.debug("GroupedOpenApi-auth");

        return GroupedOpenApi.builder()
                .group("04.common")
                .packagesToScan("com.kubeApi.common")
                .build();
    }

    @Bean
    public OpenApiCustomizer buildSecurityOpenApi() {
        // log.debug("GroupedOpenApi-buildSecurityOpenApi");
        SecurityScheme securityScheme = new SecurityScheme()
                .name(JWT_TOKEN_HEADER_PARAM)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");

        return OpenApi -> OpenApi
                .addSecurityItem(new SecurityRequirement().addList("jwt token"))
                .getComponents().addSecuritySchemes("jwt token", securityScheme);
    }

}