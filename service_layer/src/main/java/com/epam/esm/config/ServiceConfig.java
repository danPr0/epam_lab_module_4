package com.epam.esm.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Properties;

@SpringBootApplication
@PropertySource(value = "classpath:/application-service.yaml", factory = ServiceConfig.YamlPropertySourceFactory.class)
public class ServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(4, new SecureRandom());
    }

    public static class YamlPropertySourceFactory implements PropertySourceFactory {

        @Override
        public org.springframework.core.env.PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(encodedResource.getResource());

            Properties properties = factory.getObject();

            return new PropertiesPropertySource(Objects.requireNonNull(encodedResource.getResource().getFilename()),
                    Objects.requireNonNull(properties));
        }
    }
}
