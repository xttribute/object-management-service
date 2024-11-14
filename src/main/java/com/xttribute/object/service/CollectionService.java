package com.xttribute.object.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.xttribute.object.ApplicationConfiguration;

public class CollectionService {
	ApplicationConfiguration appconfig = new ApplicationConfiguration();
	MongoClient mclient = appconfig.mongoClient();
	
	@Autowired
	
	public boolean collectionExists(String dbName, String collName, ModelAndView modelAndView) {
		boolean exists = false;
		MongoTemplate mtemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		if(mtemplate.collectionExists(collName)) {
			 modelAndView.addObject("coll_302","Collection exists");
			 exists = true;
		}else {
			 modelAndView.addObject("coll_404","No collection found");
		}
		return exists;
	}
	
	public MongoCollection createColl(String dbName, String collName, ModelAndView modelAndView) {
		MongoTemplate mtemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		MongoCollection mColl= mtemplate.createCollection(collName);
		 modelAndView.addObject("coll_201","New collection created");
		return mColl;
	}
}