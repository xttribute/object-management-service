package com.xttribute.object;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }) 
public class ObjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObjectApplication.class, args);
	}

}
