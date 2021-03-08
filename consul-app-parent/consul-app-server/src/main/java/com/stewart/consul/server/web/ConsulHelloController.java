package com.stewart.consul.server.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author stewart
 */
@RestController
@RequestMapping("/consul/server")
public class ConsulHelloController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public String hello() {
        return "consul server ~ " + port;
    }

}
