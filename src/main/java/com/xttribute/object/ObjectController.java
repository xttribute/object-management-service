package com.xttribute.object;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.text.Document;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.MediaType;

import com.xttribute.object.service.DatabaseService;
import com.xttribute.object.service.DocumentService;
import com.xttribute.object.controller.SecurityController;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xttribute.object.controller.JsonController;
import com.xttribute.object.service.CollectionService;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
class ObjectController{
	DatabaseService dbService = new DatabaseService();
	CollectionService collService = new CollectionService(); 
	DocumentService docService = new DocumentService(); 
	/*
	@RequestMapping(value = "/getDB/{dbName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String checkDB(@PathVariable String dbName) {
		String dbMessage = null;
		if (dbService.databaseExists(dbName)){
			dbMessage = "Database exists";
		}
		return dbMessage;
	}
	*/
	
	@PostMapping(value="/createDB")
	public ModelAndView createDB(@RequestBody Object newObject) {
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		if (!dbService.databaseExists(newObject.getDBName(), modelAndView)){
			dbService.createDB(newObject.getDBName(), modelAndView);
		}
		return modelAndView;
	}
	
	@PostMapping(value="/createCollection")
	public ModelAndView createCollection(@RequestBody Object newObject) {
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
			if (!collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
				collService.createColl(newObject.getDBName(),newObject.getCollName(), modelAndView);
			}
		}
		return modelAndView;
	}
	
	/*
	@GetMapping("/getDB/{dbName}")
	public ResponseEntity<String> checkDB(@PathVariable String dbName){
		String dbMessage = null;
		if (dbService.databaseExists(dbName)){
			dbMessage = "Database exists";
		}
		return ResponseEntity.ok(dbMessage);
	}
	*/
	
	@PostMapping(value ="/newObject")
	public ModelAndView newObject(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException  {
		 	ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		 	if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
		 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
					String dValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUKey(), modelAndView);
					Map fDoc = docService.getDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), dValue, modelAndView);
					if (fDoc==null) {
						docService.saveDocument(newObject.getDBName(), newObject.getCollName(), newObject.getDocContents(), modelAndView);
					}
					//fDoc.get("name");
					
		 		}
		 	}
			return modelAndView;
	  }
	
	@PostMapping(value ="/matchObject")
	public ModelAndView findObject(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException  {
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
	 	if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
	 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
	 			List<String> docKeys = JsonController.getJsonKeys(newObject.getDocContents(),modelAndView);
				Map fDoc = docService.getDocumentByOperator(newObject.getDBName(), newObject.getCollName(), newObject.getDocContents(), newObject.getOperator(), modelAndView);
				if (fDoc!=null) {
					switch (newObject.getReturnType()) {
						case "token":
							String token = SecurityController.generateToken((String) fDoc.get(docKeys.get(0)));
							modelAndView.addObject(newObject.getReturnType(),token);
					}
				}
	 		}
	 	}
		return modelAndView;
	}
	
}