package com.raja;

import com.raja.config.ElasticSearchConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class SpringbootElasticsearchApplication {

    public static void main(String[] args) throws IOException {
        final ConfigurableApplicationContext applicationContext =
                SpringApplication.run(SpringbootElasticsearchApplication.class, args);
        final EmployeeService bean = applicationContext.getBean(EmployeeService.class);
        bean.save();

    }
}
