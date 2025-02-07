package com.xttribute.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {
	private String fileName;
    private String fileUrl;
    public FileDto(String fileName, String fileUrl) {
		// TODO Auto-generated constructor stub
    	this.fileName = fileName;
    	this.fileUrl = fileUrl;
	}
	public String getFileName() {
		// TODO Auto-generated method stub
		return this.fileName;
	}
	public String getFileUrl() {
		// TODO Auto-generated method stub
		return this.fileUrl;
	}
}