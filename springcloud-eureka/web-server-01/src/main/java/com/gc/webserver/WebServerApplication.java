package com.gc.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class WebServerApplication {
    public static void main(String arg[]){
        SpringApplication.run(WebServerApplication.class,arg);
    }
}
