package com.jwt.icarus.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.jwt.icarus")
public class Run {


    public static void main(String[] args) {
        SpringApplication.run(Run.class, args);
    }

}
