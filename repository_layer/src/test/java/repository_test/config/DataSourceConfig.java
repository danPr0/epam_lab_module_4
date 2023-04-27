package repository_test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Class for configuring data source in test environment.
 *
 * @author Danylo Proshyn
 */

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.config"})
public class DataSourceConfig {

}
