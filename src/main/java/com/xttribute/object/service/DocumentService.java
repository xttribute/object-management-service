package com.xttribute.object.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.xttribute.object.ApplicationConfiguration;
import com.xttribute.object.controller.JsonController;

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
	
	public void saveDocument(String dbName, String collName, String dContents, ModelAndView modelAndView) throws JsonParseException, IOException {
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		mTemplate.save(dContents, collName);		
		modelAndView.addObject("doc_201","Document created");
	}
	
	public Map<String, Object> getDocumentByOperator (String dbName, String collName, String dContents, String operator, ModelAndView modelAndView) throws JsonParseException, IOException, JSONException {
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		List<String> docKeys = JsonController.getJsonKeys(dContents,modelAndView);
		String q = null;
		for (int i=0; i< docKeys.size(); i++) {
			String dValue = JsonController.getJsonValueByKey(dContents, docKeys.get(i), modelAndView);
			if(i==0) {
				q = "{ $"+operator+":[{ "+docKeys.get(i)+":'"+dValue+"'},";
			}else {
				q +="{"+docKeys.get(i)+":'"+dValue+"'}";
			}			
		}
		q += "]}";
		BasicQuery query =new BasicQuery(q);
		if(mTemplate.findOne(query, Map.class, collName)!= null) {
			modelAndView.addObject("doc_302","Document matched");
		}else {
			modelAndView.addObject("doc_204","No document matched");
		}
		return mTemplate.findOne(query, Map.class, collName);
	}
}