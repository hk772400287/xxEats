package com.ggw.xxEats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class XxEatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxEatsApplication.class, args);
        log.info("Project booted successfully");
    }
}
