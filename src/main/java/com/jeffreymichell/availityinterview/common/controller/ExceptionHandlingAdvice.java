package com.jeffreymichell.availityinterview.common.controller;

import com.jeffreymichell.availityinterview.fileprocessor.dto.BadFileFormatException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
class ExceptionHandlingAdvice {
	@ExceptionHandler(BadFileFormatException.class)
	ResponseEntity<ErrorMessage> handleBadFileFormatException(IOException e) {
		ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(FileNotFoundException.class)
	ResponseEntity<ErrorMessage> handleFileNotFound(FileNotFoundException e) {
		ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
		return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IOException.class)
	ResponseEntity<ErrorMessage> handleIOException(IOException e) {
		ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	class ErrorMessage {
		private String message;

		ErrorMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}
