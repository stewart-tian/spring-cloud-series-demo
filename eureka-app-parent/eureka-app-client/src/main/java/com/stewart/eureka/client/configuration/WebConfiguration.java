package com.stewart.eureka.client.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author stewart
 */
@Configuration
public class WebConfiguration {

    @Bean
    public RestTemplate restIpTemplate(){
        return new RestTemplate();
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
