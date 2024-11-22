package com.xttribute.object;

import org.springframework.data.annotation.Id;

public class Object {
	@Id
	public String id;
	public String dbName;
	public String collName;
	public String docContents;
	public String uKey;
	public String operator;
	public String returnType;
	
	public Object() {}
	
	public Object(String dbName, String collName, String docContents, String uKey, String operator, String returnType) {
		this.dbName = dbName;
		this.collName = collName;
		this.docContents = docContents;
		this.uKey = uKey;
		this.operator = operator;
		this.returnType = returnType;
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
	
	public String getOperator() {
		return this.operator;
	}
	
	public String getReturnType() {
		return this.returnType;
	}
}