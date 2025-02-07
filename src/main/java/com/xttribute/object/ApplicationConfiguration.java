package com.xttribute.object;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@Configuration
@PropertySource("classpath:application.properties")
public
class ApplicationConfiguration {

  @Bean
public
static
  MongoClient mongoClient() {
      return MongoClients.create("mongodb://localhost:27017");
  }


public
static
  MongoOperations mongoTemplate(MongoClient mongoClient, String dbName) {
      return new MongoTemplate(mongoClient, dbName);
  }


}