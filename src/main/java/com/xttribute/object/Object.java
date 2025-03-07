package com.xttribute.object;

import org.springframework.data.annotation.Id;

public class Object {
	@Id
	public String id;
	public String dbName;
	public String collName;
	public String docContents;
	public String uKey;
	public String updateKey;
	public String operator;
	public String returnType;
	public String sortBy;
	public String order;
	public int limit;
	
	public Object() {}
	
	public Object(String dbName, String collName, String docContents, String uKey, String updateKey, String operator, String returnType, String sortBy, String order, int limit) {
		this.dbName = dbName;
		this.collName = collName;
		this.docContents = docContents;
		this.uKey = uKey;
		this.updateKey = updateKey;
		this.operator = operator;
		this.returnType = returnType;
		this.sortBy = sortBy;
		this.order = order;
		this.limit = limit;
	}
	
	public String getDBName() {
		return this.dbName;
	}
	
	public String getCollName() {
		return this.collName;
	}
	
	public String getDocContents() {
		return this.docContents;
	}
	
	public String getUKey() {
		return this.uKey;
	}
	
	public String getUpdateKey() {
		return this.updateKey;
	}
	
	public String getOperator() {
		return this.operator;
	}
	
	public String getReturnType() {
		return this.returnType;
	}
	public String getSortBy() {
		return this.sortBy;
	}
	public String getOrder() {
		return this.order;
	}
	public int getLimit() {
		return this.limit;
	}
}
