package com.wukef.hhforumservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class HhForumServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HhForumServiceApplication.class, args);
    }

}
