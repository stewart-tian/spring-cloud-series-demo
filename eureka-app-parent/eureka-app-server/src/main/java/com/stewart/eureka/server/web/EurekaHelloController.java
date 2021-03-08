package com.stewart.eureka.server.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author stewart
 */
@RestController
@RequestMapping("/eureka/server")
public class EurekaHelloController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public String hello() {
        return "eureka server ~ " + port;
    }

}
