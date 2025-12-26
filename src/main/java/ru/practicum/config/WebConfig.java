package ru.practicum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableWebMvc
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = false)
public class WebConfig {
}
