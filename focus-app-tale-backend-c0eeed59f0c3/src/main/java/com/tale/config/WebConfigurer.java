package com.tale.config;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.jhipster.config.JHipsterProperties;

import java.util.List;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, WebMvcConfigurer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env;

    private final JHipsterProperties jHipsterProperties;

    public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties) {
        this.env = env;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000") // Make sure this matches your client origin
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
            .allowCredentials(true);
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }

        log.info("Web application fully configured");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow all origins temporarily to test
        config.addAllowedOriginPattern("*");  // Use addAllowedOriginPattern for wider support

        // Allow all headers and methods
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // Allow credentials
        config.setAllowCredentials(true);

        // Apply configuration to all endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }

}
