package com.example.serviceribbon;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
@EnableHystrix  //断路器注解
@EnableCircuitBreaker
public class ServiceRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    // 断路器配置，当无法调用如下方法时，就会调用自定的hiError方法。
    @HystrixCommand(fallbackMethod = "hiError")
    @RequestMapping("/hi")
    public String hiService(String name)
    {
        System.out.println("name = " + name);
//        ResponseEntity<Object> forEntity = restTemplate.getForEntity("http://SERVICE-HI/hi?name=" + name, Object.class);
//        Object forObject = restTemplate.getForObject("http://SERVICE-HI/hi?name=" + name, Object.class);
//        return forObject.toString();

        return restTemplate.getForObject("http://SERVICE-HI/hi?name=" + name, String.class);
    }


    public String hiError(String name)
    {
        return "hey " +
                name + ", there is some problem with hi page";
    }
}
