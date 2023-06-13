package com.epam.esm;

import com.epam.esm.util_repository.FillTablesUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Base class for configuring data source.
 *
 * @author Danylo Proshyn
 */

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan
@SpringBootApplication()
@EnableJpaAuditing
//@PropertySource("classpath:/datasource-dev.properties")
@PropertySource("classpath:/datasource-${spring.profiles.active}.properties")
public class RepositoryConfig {

/*
    @Autowired
    private FillTablesUtil fillTablesUtil;

    public static void main(String[] args) {
        SpringApplication.run(RepositoryConfig.class, args);
    }

    @PostConstruct
    private void fillTables() {
        fillTablesUtil.fillTables();
    }

 */



    /*
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "org.hibernate.envers.audit_table_suffix", "_AUDIT_LOG");
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }
     */
}