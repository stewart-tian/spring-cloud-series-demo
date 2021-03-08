package com.stewart.eureka.client.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author stewart
 */
@RestController
@RequestMapping("/eureka/client")
public class EurekaClientHelloController {

    @Autowired
    private RestTemplate restIpTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/helloIp")
    public String invokeHello() {
        String ipUrl = "http://localhost:8770";
        return "Eureka Client invoke ip : " + restIpTemplate.getForObject(ipUrl + "/eureka/server/hello", String.class);
    }

    @GetMapping("/helloDomain")
    public String invokeHelloDomain() {
        String domainUrl = "http://eureka-app-server";
        return "Eureka client invoke domain : " + restTemplate.getForObject(domainUrl + "/eureka/server/hello", String.class);
    }

}
