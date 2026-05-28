// A_T_Service/trade-service/src/main/java/com/morganstanley/tradeservice/config/AppConfig.java
package com.morganstanley.tradeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
