package com.rkcreadev.demo.akka;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan
public class AkkaApplication {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(AkkaApplication.class);
    }
}
