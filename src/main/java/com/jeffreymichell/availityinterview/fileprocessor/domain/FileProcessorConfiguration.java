package com.jeffreymichell.availityinterview.fileprocessor.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FileProcessorConfiguration {
	@Bean
	FileProcessorFacade fileProcessorFacade() {
		EnrollmentFileExtractor enrollmentFileExtractor = new EnrollmentFileExtractor();
		OutputFileWriter outputFileWriter = new OutputFileWriter();
		return new FileProcessorFacade(enrollmentFileExtractor, outputFileWriter);
	}
}
