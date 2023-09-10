package com.epam.esm;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import software.amazon.codeguruprofilerjavaagent.Profiler;

@SpringBootApplication
@EnableEncryptableProperties
public class AppLauncher extends SpringBootServletInitializer  {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(AppLauncher.class);
    }

    public static void main(String[] ars) {

/*
        Profiler.builder()
                .profilingGroupName("MyProfilingGroup")
                .withHeapSummary(true)
                .build()
                .start();
*/

        SpringApplication.run(AppLauncher.class, ars);
    }
}