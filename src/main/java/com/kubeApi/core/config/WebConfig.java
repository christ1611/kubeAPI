package com.kubeApi.core.config;

import com.kubeApi.core.mapper.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Slf4j
@Configuration
@EnableWebMvc
public class WebConfig {
    @Bean
    public MappingJackson2JsonView jsonView() {
        MappingJackson2JsonView ov = new MappingJackson2JsonView();
        ov.setContentType("application/json;charset=UTF-8");
        ov.setObjectMapper(MapperUtil.mapper);

        return ov;
    }
}