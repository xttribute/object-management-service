package com.xttribute.object.service;

import java.util.Map;

import javax.swing.text.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.xttribute.object.ApplicationConfiguration;

public class DocumentService {
	ApplicationConfiguration appconfig = new ApplicationConfiguration();
	MongoClient mclient = appconfig.mongoClient();
	
	
	public Map<String, Object> getDocument (String dbName, String collName, String uKey, String uValue, ModelAndView modelAndView) {
		Query query = new Query(Criteria.where(uKey).is(uValue));
		//query.fields().exclude("_class");
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		if  (mTemplate.findOne(query, Map.class, collName) != null) {
			modelAndView.addObject("doc_302","Document exists");
		}
		return mTemplate.findOne(query, Map.class, collName);
	}
	
	public void saveDocument(String dbName, String collName, String dContents, ModelAndView modelAndView) {
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		mTemplate.save(dContents, collName);
		modelAndView.addObject("doc_201","Document created");
	}
}