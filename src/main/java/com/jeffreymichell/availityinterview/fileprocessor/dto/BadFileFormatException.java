package com.jeffreymichell.availityinterview.fileprocessor.dto;

public class BadFileFormatException extends RuntimeException {
	public BadFileFormatException(String line) {
		super("File line has unexpected format. Expected: '%s,%s,%d,%s', Actual: " + line);
	}
}
