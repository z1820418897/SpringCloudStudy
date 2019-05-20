package com.gc.consumerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableHystrixDashboard
public class ConsumerServerApplication {
    public static void main(String arg[]){
        SpringApplication.run(ConsumerServerApplication.class,arg);
    }
}
