package com.epam.esm.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Objects;
import java.util.Properties;

/**
 * Base class for configuring data source.
 *
 * @author Danylo Proshyn
 */

@Configuration
@EnableAutoConfiguration
@EnableJpaAuditing
@PropertySource(value = "classpath:/application-repository-${spring.profiles.active}.yaml",
                factory = RepositoryConfig.YamlPropertySourceFactory.class)
public class RepositoryConfig {

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