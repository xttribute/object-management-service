package com.xttribute.object;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.text.Document;
import lombok.RequiredArgsConstructor;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xttribute.object.service.DatabaseService;
import com.xttribute.object.service.DocumentService;
import com.xttribute.object.controller.SecurityController;
import com.xttribute.object.dto.FileDto;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.client.result.DeleteResult;
import com.xttribute.object.controller.JsonController;
import com.xttribute.object.service.CollectionService;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.xttribute.object.service.FileService;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
class ObjectController{

	DatabaseService dbService = new DatabaseService();
	CollectionService collService = new CollectionService(); 
	DocumentService docService = new DocumentService(); 
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectController.class);

	private  FileService fileService = new FileService();
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
	public ModelAndView newObject(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException, IllegalArgumentException, IllegalAccessException  {
		 	ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		 	if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
		 	
		 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
					Map fDoc =null;
					String dValue = null;
					String uKey = newObject.getUKey();
					if(uKey.equals("0")) {
						fDoc=null;
					}else {
						dValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUKey(), modelAndView);
						fDoc = docService.getDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), dValue, modelAndView);
					}
					if (fDoc==null) {
						String savedObjectId = docService.saveDocumentTest(newObject.getDBName(), newObject.getCollName(), newObject.getDocContents(), modelAndView);
						modelAndView.addObject("_id", savedObjectId);
					}
					
		 		}
		 	}
			return modelAndView;
	  }
	
	@PostMapping(value ="/getOneObject")
	public ModelAndView getOneObject(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException  {
		 	ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		 	if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
		 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
		 			String dValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUKey(), modelAndView);
		 			Map fDoc = docService.getDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), dValue, modelAndView);
					if (fDoc!=null) {			
						String convertedId = JsonController.getValue(fDoc, "_id");
						fDoc = JsonController.setValue(fDoc, "_id", convertedId);
						modelAndView.addObject("object", fDoc);
					}
					//fDoc.get("name");
					
		 		}
		 	}
			return modelAndView;
	  }
	@PostMapping(value ="/removeObject")
	public ModelAndView removeObject(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException  {
		 	ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		 	if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
		 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
					String dValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUKey(), modelAndView);
					DeleteResult deleteResult = docService.removeDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), dValue, modelAndView);
		 		}
		 	}
			return modelAndView;
	  }
	
	@PostMapping(value ="/getObjects")
	public ModelAndView getObjects(@RequestBody Object newObject, @RequestParam("page") int page) throws JsonParseException, IOException, JSONException  {
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
	 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
				List<Map> fDoc = docService.getDocumentByOperator(newObject.getDBName(), newObject.getCollName(), newObject.getDocContents(), newObject.getOperator(), newObject.getSortBy(), newObject.getOrder(), newObject.getLimit(), page, modelAndView);
				if (fDoc!=null) {
					switch (newObject.getReturnType()) {
						case "list":
							for(Map<String, Object> map : fDoc) {					
								String convertedId = JsonController.getValue(map, "_id");
							    map = JsonController.setValue(map, "_id", convertedId);
								//modelAndView.addObject("objects"+i,map);
							}
							modelAndView.addObject("objects",fDoc);
							//modelAndView.addObject("number_of_results", i);
							
					}
				}
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
				List<Map> fDoc = docService.getDocumentByOperator(newObject.getDBName(), newObject.getCollName(), newObject.getDocContents(), newObject.getOperator(), "_id", "ASC",1,1, modelAndView);				
				if (fDoc!=null) {
					switch (newObject.getReturnType()) {
						case "token":
							for(Map<String, Object> map : fDoc) {	
								String matchKey = JsonController.getValue(map, docKeys.get(0));
								String matchId = JsonController.getValue(map, "_id");
								String token = SecurityController.generateToken(matchKey);
								modelAndView.addObject(newObject.getReturnType(),token);
								modelAndView.addObject("_id",matchId);
							}
						case "result":
							for(Map<String, Object> map : fDoc) {	
								String matchId = JsonController.getValue(map, "_id");				
								map = JsonController.setValue(map, "_id", matchId);
								modelAndView.addObject("object",map);
							}
					}
				}
	 		}
	 	}
		return modelAndView;
	}
	
	@PostMapping(value ="/editObject")
	public ModelAndView editObject(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException  {
		 	ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
		 	if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
		 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
					String dValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUKey(), modelAndView);
					Map fDoc = docService.getDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), dValue, modelAndView);
					if (fDoc!=null) {
						String updateValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUpdateKey(), modelAndView);
						docService.updateDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), fDoc.get(newObject.getUKey()).toString(), newObject.getUpdateKey(), updateValue, modelAndView);
					}
					//fDoc.get("name");
					
		 		}
		 	}
			return modelAndView;
	  }
	
	 @PostMapping(value ="/uploadFile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView uploadFile(@RequestParam("dbName") String dbName, @RequestParam("collName") String collName , @RequestParam("files")MultipartFile[] files, @RequestParam("xid") String xid, @RequestParam("type") String type, @RequestParam("folder") String folder,  @RequestParam(value = "compId", required = false, defaultValue = "") String compId){
		ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        LOGGER.debug("Call addFile API");   
        
        try {
        	List<FileDto> uploadedFile = fileService.uploadFiles(files, folder, modelAndView);
        			for(FileDto file :uploadedFile) {
        				switch (type) {
						case "thumbnail":
							docService.updateDocument(dbName, collName, "_id", xid, type, file.getFileName(), modelAndView);
							modelAndView.addObject(type,file.getFileName());
							break;
						case "photo" :
							docService.updateDocument(dbName, collName, "_id", compId, type, file.getFileName(), modelAndView);
							modelAndView.addObject(type,file.getFileName());
							break;
						case "sound" :
							docService.updateDocument(dbName, collName, "_id", compId, type, file.getFileName(), modelAndView);
							modelAndView.addObject(type,file.getFileName());
							break;
						case "video" :
							docService.updateDocument(dbName, collName, "_id", compId, type, file.getFileName(), modelAndView);
							modelAndView.addObject(type,file.getFileName());
							break;
        				}
        				
        			}
        	
        }catch (Exception e){
        	
        }
       return modelAndView; 
  }
	
	 @PostMapping(value ="/updateStats")
		public ModelAndView updateStats(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException  {
			 	ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
			 	if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
			 		if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
						String dValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUKey(), modelAndView);
						Map fDoc = docService.getDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), dValue, modelAndView);			
						if (fDoc!=null) {
							int nextCount;
							int count = Integer.parseInt(JsonController.getJsonValueByKey(newObject.getDocContents(), "count", modelAndView));
							switch(newObject.getUpdateKey()) {
								case "view" :
									if(fDoc.get("view")==null) {
										nextCount= 0 + count; 
									}else {
									  nextCount = Integer.parseInt(fDoc.get("view").toString())+ count;
									}
									docService.updateDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), fDoc.get(newObject.getUKey()).toString(), newObject.getUpdateKey(), Integer.toString(nextCount), modelAndView);
									modelAndView.addObject("success","view count updated");
							}					
							
						}
						//fDoc.get("name");
						
			 		}
			 	}
				return modelAndView;
		  }
		
	 @PostMapping(value ="/updateObject")
	    public ModelAndView updateObject(@RequestBody Object newObject) throws JsonParseException, IOException, JSONException  {
	        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
	        if (dbService.databaseExists(newObject.getDBName(), modelAndView)){
	            if (collService.collectionExists(newObject.getDBName(),newObject.getCollName(), modelAndView)){
	                // Resolve the lookup value from docContents using uKey
	                String dValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUKey(), modelAndView);
	                Map fDoc = docService.getDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), dValue, modelAndView);
	                if (fDoc!=null) {
	                    // Resolve the update value from docContents using updateKey
	                    String updateValue = JsonController.getJsonValueByKey(newObject.getDocContents(), newObject.getUpdateKey(), modelAndView);
	                    // Perform the update
	                    docService.updateDocument(newObject.getDBName(), newObject.getCollName(), newObject.getUKey(), fDoc.get(newObject.getUKey()).toString(), newObject.getUpdateKey(), updateValue, modelAndView);
	                    modelAndView.addObject("success","updated");
	                } else {
	                    modelAndView.addObject("error","document_not_found");
	                }
	            }
	        }
	        return modelAndView;
	    }
	
}