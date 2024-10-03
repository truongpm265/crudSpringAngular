package com.example.searchdq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.searchdq.entity")
public class SearchDqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchDqApplication.class, args);
    }

}
