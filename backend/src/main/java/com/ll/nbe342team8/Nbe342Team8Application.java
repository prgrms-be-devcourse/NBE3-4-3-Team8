package com.ll.nbe342team8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class Nbe342Team8Application {

    public static void main(String[] args) {
        SpringApplication.run(Nbe342Team8Application.class, args);
    }

}
