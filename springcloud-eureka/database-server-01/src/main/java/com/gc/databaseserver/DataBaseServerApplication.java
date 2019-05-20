package com.gc.databaseserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DataBaseServerApplication {
    public static void main(String arg[]){
        SpringApplication.run(DataBaseServerApplication.class,arg);
    }
}
