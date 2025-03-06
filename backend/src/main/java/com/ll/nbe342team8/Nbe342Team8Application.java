package com.ll.nbe342team8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class Nbe342Team8Application {

    public static void main(String[] args) {
        SpringApplication.run(Nbe342Team8Application.class, args);
    }

}
