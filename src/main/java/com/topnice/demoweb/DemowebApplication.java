package com.topnice.demoweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemowebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemowebApplication.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
//    extends SpringBootServletInitializer
//        return builder.sources(this.getClass());
//    }

}
