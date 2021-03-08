package com.stewart.consul.client.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author stewart
 */
@Configuration
public class WebConfiguration {

    /**
     * 无@LoadBalanced注解，只能根据调用指定实例（此处为指定ip+port）
     */
    @Bean
    public RestTemplate reqIpTemplate() {
        return new RestTemplate();
    }

    /**
     * 通过@LoadBalanced注解，可根据服务实例名进行调用，默认轮询调用服务实例
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
