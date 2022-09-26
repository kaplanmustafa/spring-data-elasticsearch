package com.mustafakaplan.springbootelasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.mustafakaplan.springbootelasticsearch.dao")
public class SpringBootElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootElasticsearchApplication.class, args);
    }

}
