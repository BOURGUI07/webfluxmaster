package com.example.playground.sec07.config;

import com.example.playground.sec07.dto.ProductResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {
    @Bean
    public Sinks.Many<ProductResponse> sink(){
        return Sinks.many().replay().limit(1);
    }
}
