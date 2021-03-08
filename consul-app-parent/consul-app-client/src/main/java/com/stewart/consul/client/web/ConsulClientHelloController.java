package com.stewart.consul.client.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author stewart
 */
@RestController
@RequestMapping("/consul/client")
public class ConsulClientHelloController {

    @Autowired
    private RestTemplate reqIpTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 调用指定ip+端口对应的服务实例
     */
    @GetMapping("/helloIp")
    public String invokeHelloIp() {
        String ipUrl = "http://localhost:8510";
        return "Consul Client invoke ip : " + reqIpTemplate.getForObject(ipUrl + "/consul/server/hello", String.class);
    }

    /**
     * 轮询调用指定服务实例
     */
    @GetMapping("/helloDomain")
    public String invokeHelloDomain() {
        String serverUrl = "http://consul-app-server";
        return "Consul Client invoke Domain : " + restTemplate.getForObject(serverUrl + "/consul/server/hello", String.class);
    }

}
