package com.xttribute.object;

import org.springframework.data.annotation.Id;

public class Object {
	@Id
	public String id;
	public String dbName;
	public String collName;
	public String docContents;
	public String uKey;
	
	public Object() {}
	
	public Object(String dbName, String collName, String docContents, String uKey) {
		this.dbName = dbName;
		this.collName = collName;
		this.docContents = docContents;
		this.uKey = uKey;
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
	
}