package com.example.authdemospring.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationErrorHandler {
    //  Custom exceptions can be used to fine-tune the response here
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleGeneralExceptions() {
        return ResponseEntity.status(401).build();
    }
}
