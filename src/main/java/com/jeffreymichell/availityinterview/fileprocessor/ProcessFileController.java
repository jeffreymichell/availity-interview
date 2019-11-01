package com.jeffreymichell.availityinterview.fileprocessor;

import com.jeffreymichell.availityinterview.fileprocessor.domain.FileProcessorFacade;
import com.jeffreymichell.availityinterview.fileprocessor.dto.EnrollmentFileRequest;
import com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
public class ProcessFileController {
	private static final Logger log = getLogger(ProcessFileController.class);
	private static final String FILE_PROCESSED = "File successfully processed";
	private FileProcessorFacade fileProcessorFacade;

	public ProcessFileController(FileProcessorFacade fileProcessorFacade) {
		this.fileProcessorFacade = fileProcessorFacade;
	}

	@PostMapping("processEnrollmentFile")
	@ResponseBody
	ResponseEntity<Object> processEnrollmentFile(@RequestBody EnrollmentFileRequest request) throws IOException {
		log.info("processEnrollmentFile() invoked with {}", request);
		Map<String, List<InsuranceEnrollmentRecord>> processedRecords =
				fileProcessorFacade.getCompanyRecordsFromFile(request.getEnrollmentFilePath());

		fileProcessorFacade.writeOutputFilePerCompany(request.getOutputfilePath(), processedRecords);
		return new ResponseEntity<>(FILE_PROCESSED, HttpStatus.OK);
	}
}
