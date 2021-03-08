package com.stewart.nacos.config.web;

import com.stewart.nacos.config.configuration.SampleConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author stewart
 */
@RestController
@RequestMapping("/nacos/config/refresh")
public class NacosConfigController {

    @Autowired
    private SampleConfiguration sampleConfiguration;

    @GetMapping("/param")
    public String getSample(){
        return sampleConfiguration.toString();
    }

}
