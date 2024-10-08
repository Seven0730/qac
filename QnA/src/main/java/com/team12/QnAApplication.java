package com.team12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QnAApplication {

    public static void main( String[] args ){
        SpringApplication.run(QnAApplication.class, args);
    }
}
