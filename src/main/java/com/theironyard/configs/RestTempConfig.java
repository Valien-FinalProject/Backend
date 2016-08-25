package com.theironyard.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by EddyJ on 8/25/16.
 */
@Configuration
public class RestTempConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
