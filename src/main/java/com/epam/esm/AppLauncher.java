package com.epam.esm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Properties;

@SpringBootApplication()
public class AppLauncher extends SpringBootServletInitializer  {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(AppLauncher.class);
    }

    public static void main(String[] ars) {

//        Properties props = new Properties();
//        props.put("spring.profiles.active", "dev");

//        SpringApplication springApp = new SpringApplication(AppLauncher.class);
//        springApp.setDefaultProperties(props);
//        springApp.run(ars);
//        springApp.setAdditionalProfiles("dev");
        SpringApplication.run(AppLauncher.class, ars);
    }
}