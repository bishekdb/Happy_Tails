package com.happytails.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.happytails.website.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class HappyTailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyTailsApplication.class, args);
    }
} 