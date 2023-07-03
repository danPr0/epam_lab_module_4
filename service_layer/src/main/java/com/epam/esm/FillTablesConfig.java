package com.epam.esm;

import com.epam.esm.util_service.FillTablesUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class FillTablesConfig {

    private final FillTablesUtil fillTablesUtil;

    @Autowired
    public FillTablesConfig(FillTablesUtil fillTablesUtil) {this.fillTablesUtil = fillTablesUtil;}

    public static void main(String[] args) {

        SpringApplication springApp = new SpringApplication(ServiceConfig.class);

        Properties properties = new Properties();
        properties.put("spring.profiles.active", "dev");
        springApp.setDefaultProperties(properties);

        springApp.run(args);
    }

//    @PostConstruct
//    private void fillTables() {
//        fillTablesUtil.fillTables();
//    }
}
