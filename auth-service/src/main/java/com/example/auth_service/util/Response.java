package com.example.auth_service.util;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response {
  
  private String message;
  private int statusCode;
  private boolean isSuccess;
  private Object data;

  public void sendSuccessResponse(String message , int statusCode){
    this.isSuccess = true;
    this.message =message;
    this.statusCode = statusCode;
  }
  public void sendSuccessResponse(String message, int statusCode , Object data){
    this.data = data;
    this.isSuccess = true;
    this.message = message;
    this.statusCode = statusCode;
  }

  public void sendErrorResponse(String message , int statusCode){
    this.isSuccess = false;
    this.message =message;
    this.statusCode = statusCode;

  }
  public void sendErrorResponse(String message, int statusCode , Object data){
    this.data = data;
    this.isSuccess = false;
    this.message = message;
    this.statusCode = statusCode;
  }
  public ResponseEntity<Response> sendApiResponse(Response response){
    return ResponseEntity.status(response.getStatusCode()).body(response);
  } 

}
