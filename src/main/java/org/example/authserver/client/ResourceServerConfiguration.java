package org.example.authserver.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JQ
 */
@Configuration
public class ResourceServerConfiguration {

    @Bean
    ResourceServerFallbackFactory resourceServerFallbackFactory() {
        return new ResourceServerFallbackFactory();
    }
}
