package com.example.auth_service.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.auth_service.util.Response;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobleExceptionHandler {
  


  @ExceptionHandler(Exception.class)
  public ResponseEntity<Response> handleError(Exception ex){
    Response response = new Response();
    response.sendErrorResponse(ex.getMessage(), 400);
    return response.sendApiResponse(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Response> handleError(ConstraintViolationException constraintViolationException){
    Response response = new Response();
    response.sendErrorResponse("Constranint Vioaltion", 400, constraintViolationException.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
    return response.sendApiResponse(response);
  }
  
}
