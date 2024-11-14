package com.xttribute.object.service;

import com.mongodb.client.MongoClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.xttribute.object.ApplicationConfiguration;
import com.xttribute.object.exception.ResourceNotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class DatabaseService {
	ApplicationConfiguration appconfig = new ApplicationConfiguration();
	MongoClient mclient = appconfig.mongoClient();
	
	@Autowired

	public boolean databaseExists(String dbName, ModelAndView modelAndView) {
		MongoIterable<String> mdb = mclient.listDatabaseNames();
		boolean exists = false;
		for (String str : mdb) {
			 if (str.equals(dbName)) {
	                exists = true;
	                modelAndView.addObject("db_302","Database exists");
	                break;
	         }
		}
		
		if (!exists) {
			modelAndView.addObject("db_404","Database does not exist");
			//throw new ResourceNotFoundException("Database not found");
		}
		return exists;
		
	}
	
	public MongoDatabase createDB(String dbName,  ModelAndView modelAndView) {
		MongoTemplate mtemplate = getMongoTemplate(dbName);
		MongoDatabase mdb = mclient.getDatabase(dbName);
		mtemplate.createCollection("xtrSystem");
		modelAndView.addObject("db_201","New database created");
		return mdb;
	}
	
	public MongoDatabase getDB(String dbName) {
		MongoDatabase mdb = mclient.getDatabase(dbName);
		return mdb;		
	}
	
	public MongoTemplate getMongoTemplate(String dbName) {
		MongoTemplate mtemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		return mtemplate;
	}
	
}
