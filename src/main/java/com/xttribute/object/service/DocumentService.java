package com.xttribute.object.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
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
		}else {
			modelAndView.addObject("doc_404","Document does not exist");
		}
		return mTemplate.findOne(query, Map.class, collName);
	}
	
	public DeleteResult removeDocument (String dbName, String collName, String uKey, String uValue, ModelAndView modelAndView) {
		Query query = new Query(Criteria.where(uKey).is(uValue));
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		DeleteResult deleteResult = mTemplate.remove(query,collName);
		if(deleteResult.getDeletedCount()>0) {
			modelAndView.addObject("doc_204","Document deleted");
		}else {
			modelAndView.addObject("doc_304","Deletion failed");
		}
		return deleteResult;
	}
	
	public void updateDocument(String dbName, String collName, String uKey, String uValue, String updateKey, String updateValue, ModelAndView modelAndView) throws JsonParseException, IOException {
		Query query = new Query(Criteria.where(uKey).is(uValue));
		Update update = new Update().set(updateKey, updateValue);
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		mTemplate.updateMulti(query, update, collName);		
		modelAndView.addObject("doc_202","Document updated");
	}
	
	public void saveDocument(String dbName, String collName, String dContents, ModelAndView modelAndView) throws JsonParseException, IOException {
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);		
		mTemplate.save(dContents, collName);		
		modelAndView.addObject("doc_201","Document created");
	}
	
	public String saveDocumentTest(String dbName, String collName, String dContents, ModelAndView modelAndView) throws JsonParseException, IOException, IllegalArgumentException, IllegalAccessException {
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);		
		Object savedObject =  mTemplate.save(dContents, collName);
		Class<?> clazz = savedObject.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String _id =null;
		for (Field field : fields) {
			 if(field.toString().equals("private final java.util.LinkedHashMap org.bson.Document.documentAsMap")){
				 field.setAccessible(true);
				 Map value = (Map) field.get(savedObject);
				 _id= value.get("_id").toString();
			 }
		}
		modelAndView.addObject("doc_201","Document created");
		return _id;
	}
	public List<Map> getDocumentByOperator (String dbName, String collName, String dContents, String operator, ModelAndView modelAndView) throws JsonParseException, IOException, JSONException {
		MongoTemplate mTemplate = (MongoTemplate) appconfig.mongoTemplate(mclient, dbName);
		List<String> docKeys = JsonController.getJsonKeys(dContents,modelAndView);
		Query query; 
		if(operator=="none") {
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
		 query =new BasicQuery(q);

		}else {
			String dValue = JsonController.getJsonValueByKey(dContents, docKeys.get(0), modelAndView);
		    query = new Query(Criteria.where(docKeys.get(0)).is(dValue));
		}
	
		if(mTemplate.find(query, Map.class, collName)!= null) {
			modelAndView.addObject("doc_302","Document matched");
		}else {
			modelAndView.addObject("doc_204","No document matched");
		}
		return mTemplate.find(query, Map.class, collName);
	}
}