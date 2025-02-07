package com.xttribute.object.exception;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class ApiException {

    private String errorMessage;
    private Integer statusCode;
    private ZonedDateTime zonedDateTime;
	public void setErrorMessage(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setStatusCode(int value) {
		// TODO Auto-generated method stub
		
	}
	public void setZonedDateTime(ZonedDateTime now) {
		// TODO Auto-generated method stub
		
	}
}