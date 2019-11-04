package com.jeffreymichell.availityinterview;

import com.jeffreymichell.availityinterview.fileprocessor.ProcessFileController;
import com.jeffreymichell.availityinterview.fileprocessor.dto.EnrollmentFileRequest;
import com.jeffreymichell.availityinterview.lispchecker.LispCheckController;
import com.jeffreymichell.availityinterview.lispchecker.dto.LispCodeCheckRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvailityInterviewControllerTests {
	private static final String PASSING_LISP_TEST_CASE = "{stuff(thing[0])+{other[0-0]}}";
	private static final String PASSING_LISP_EXPECTED_RESPONSE = "The LISP code passed validation";
	private static final String ENROLLMENT_FILE_PATH =
			"src/main/resources/enrollmentTest/input/insurance_enrollment_file.txt";
	private static final String OUTPUT_FILE_PATH = "src/main/resources/enrollmentTest/output/";
	private static final String FILE_ENROLLMENT_PASSING_RESPONSE = "File successfully processed";

	@Autowired
	private LispCheckController lispCheckController;

	@Autowired
	private ProcessFileController processFileController;

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Test
	void contextLoads() {
		Assertions.assertNotNull(lispCheckController);
		Assertions.assertNotNull(processFileController);
	}

	@Test
	void testLispCheckRestController() throws URISyntaxException {
		LispCodeCheckRequest request = LispCodeCheckRequest.newBuilder()
				.withLispCode(PASSING_LISP_TEST_CASE)
				.build();
		HttpEntity<LispCodeCheckRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> response = this.restTemplate.postForEntity(createURIWithPort("/validateLispCode"), request, String.class);

		String responseBody = response.getBody();
		Assertions.assertTrue(PASSING_LISP_EXPECTED_RESPONSE.equals(responseBody));
	}

	@Test
	void testProcessFileController() throws URISyntaxException {
		EnrollmentFileRequest request = EnrollmentFileRequest.newBuilder()
				.withEnrollmentFilePath(ENROLLMENT_FILE_PATH)
				.withOutputfilePath(OUTPUT_FILE_PATH)
				.build();
		HttpEntity<EnrollmentFileRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> response = this.restTemplate.postForEntity(createURIWithPort("/processEnrollmentFile"), request, String.class);

		String responseBody = response.getBody();
		Assertions.assertTrue(FILE_ENROLLMENT_PASSING_RESPONSE.equals(responseBody));
	}

	private URI createURIWithPort(String uri) throws URISyntaxException {
		String urlString = "http://localhost:" + port + uri;
		return new URI(urlString);
	}
}
