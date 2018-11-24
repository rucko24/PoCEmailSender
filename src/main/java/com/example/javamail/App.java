package com.example.javamail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@PropertySource("classpath:mail.properties")
public class App {


    public static void main(String ...blabl) {
        SpringApplication.run(App.class);
    }

}
