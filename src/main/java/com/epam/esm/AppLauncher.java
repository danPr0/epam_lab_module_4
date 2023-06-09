package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@Configuration
//@ComponentScan
//@EnableAutoConfiguration
@SpringBootApplication()
public class AppLauncher extends SpringBootServletInitializer  {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(AppLauncher.class);
    }

    public static void main(String[] ars) {

//        SpringApplication springApp = new SpringApplication(AppLauncher.class);
//        springApp.setAdditionalProfiles("prod");
//        springApp.run(ars);
        SpringApplication.run(AppLauncher.class, ars);
    }
}