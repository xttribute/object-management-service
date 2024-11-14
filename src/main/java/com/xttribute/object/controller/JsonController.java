package com.xttribute.object.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonController {
	public static List<String> getJsonKeys (String docContent, ModelAndView modelAndView) throws JsonParseException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String docC =docContent.replace('\'', '"');
		JsonFactory factory = mapper.getJsonFactory();
		JsonParser parserContents = factory.createJsonParser(docC);
		List<String> keys = new ArrayList<>();
		JsonNode jsonNode = mapper.readTree(parserContents);
		Iterator<String> iterator = jsonNode.fieldNames();
		iterator.forEachRemaining(e -> keys.add(e));
		return keys;
	}
	
	public static String getJsonValueByKey(String docContent, String key, ModelAndView modelAndView) throws JSONException {
		JSONObject jContents = new JSONObject(docContent);
		String dValue = jContents.getString(key);
		return dValue;
	}
}