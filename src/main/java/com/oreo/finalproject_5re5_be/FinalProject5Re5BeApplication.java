package com.oreo.finalproject_5re5_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class FinalProject5Re5BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProject5Re5BeApplication.class, args);
    }

}
