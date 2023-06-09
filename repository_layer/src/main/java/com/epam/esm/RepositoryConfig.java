package com.epam.esm;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Base class for configuring data source.
 *
 * @author Danylo Proshyn
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource("classpath:/datasource-${spring.profiles.active}.properties")
public class RepositoryConfig {

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