package com.stewart.nacos.config.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author stewart
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "stewart.nacos.config.demo")
public class SampleConfiguration {

    private Integer num;

    private String str;

    /**
     * 更改参数后第一次使用该Bean会调用init方法
     */
    @PostConstruct
    public void init() {
        System.out.printf("[SampleConfiguration init] nacos config demo num : %d , str : %s%n", num, str);
    }

    /**
     * nacos更改参数发布时会调用destroy方法
     */
    @PreDestroy
    public void destroy() {
        System.out.printf("[SampleConfiguration destroy] nacos config demo num : %d , str : %s%n", num, str);
    }

}
