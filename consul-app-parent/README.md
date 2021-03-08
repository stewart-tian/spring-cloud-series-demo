# consul 做服务注册中心简单使用

## 一、consul注册中心

①：在consul官网下载consul压缩包，解压压缩包获取consul.exe \
②：通过cmd执行启动consul，命令： consul agent -dev \
③：访问localhost:8500 注册中心查看服务列表

## 二、开发服务提供者

### 2.1 定义项目依赖管理

#### 2.1.1 父项目处理

spring-cloud-series-demo/pom.xml

```xml

<properties>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <spring.boot.version>2.3.7.RELEASE</spring.boot.version>
    <spring-cloud-alibaba.version>2.2.2.RELEASE</spring-cloud-alibaba.version>
    <spring.cloud.version>Hoxton.SR9</spring.cloud.version>
    <lombok.version>1.18.16</lombok.version>
</properties>

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring.boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring.cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </dependency>
</dependencies>
</dependencyManagement>
```

consul模块项目parent只做项目module管理

#### 2.1.2 服务提供项目处理

consul-app-server模块依赖

```xml

<dependencies>
    <!-- consul服务注册 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- consul通过actuator的health端口检测服务状态 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

consul-app-server配置

```yaml
server:
  port: 8510

spring:
  application:
    name: consul-app-server
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}

---
spring:
  profiles: one

server:
  port: 8511

---
spring:
  profiles: two

server:
  port: 8512

---
spring:
  profiles: three

server:
  port: 8513
```

通过启动时设置spring.profiles.active参数来启动服务的多实例；\
或不指定spring.profiles.active 通过指定server.port不同的端口也可以启动多实例

提供一个RestController接口，启动类上添加@EnableDiscoveryClient注解，启动服务

```java

@EnableDiscoveryClient
@SpringBootApplication
public class ConsulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsulServerApplication.class, args);
    }

}
```

#### 2.1.3 服务消费者项目处理

consul-app-client模块依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

consul-app-client配置

```yaml
server:
  port: 8520

spring:
  application:
    name: consul-app-client
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
```

配置RestTemplate

```java

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
```

提供接口，调用服务提供者

```java

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
```

## 三、consul做注册中心（单实例注册中心）

启动注册中心、服务提供者、服务消费者后\
① 不做任何调用，停用注册中心，服务消费者将不可调用服务提供者 ② 服务消费者成功调用服务提供者后，停用注册中心，服务消费者可以调用到服务提供者的每一个服务实例；\
之后再启动注册中心，服务消费者将不可调用服务提供者