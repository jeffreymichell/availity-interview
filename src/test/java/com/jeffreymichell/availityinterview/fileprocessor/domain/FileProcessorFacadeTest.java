package com.jeffreymichell.availityinterview.fileprocessor.domain;

import com.jeffreymichell.availityinterview.fileprocessor.dto.BadFileFormatException;
import com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FileProcessorFacadeTest {
	private static final String ENROLLMENT_FILE_PATH =
			"src/main/resources/enrollmentTest/input/insurance_enrollment_file.txt";
	private static final String BROKEN_ENROLLMENT_FILE_PATH =
			"src/main/resources/enrollmentTest/input/broken_insurance_enrollment_file.txt";
	private static final String OUTPUT_FILE_PATH = "src/main/resources/enrollmentTest/output/";

	@Autowired
	FileProcessorFacade fileProcessorFacade;

	@BeforeAll
	public static void init() throws IOException {
		clearOutputDirectory(OUTPUT_FILE_PATH);
	}

	@AfterAll
	public static void cleanup() throws IOException {
		//Leave output files in place for manual inspection
		//To automatically delete output files instead, uncomment the below line
		//clearOutputDirectory(outputFilePath);
	}

	private static void clearOutputDirectory(String outputDirectory) throws IOException {
		Path directory = Paths.get(outputDirectory);
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@DisplayName("Test to extract deduplicated, sorted records, grouped by Insurance Company, from input Enrollment File")
	@Test
	void getCompanyRecordsFromFile() throws IOException {
		Map<String, List<InsuranceEnrollmentRecord>> recordsByCompany =
				fileProcessorFacade.getCompanyRecordsFromFile(ENROLLMENT_FILE_PATH);

		//The map should contain 8 keys, corresponding to the 8 Insurance Companies in the input file
		Assertions.assertEquals(8, recordsByCompany.size());

		//Spot check that one of the companies contains the correct number of records
		Assertions.assertEquals(recordsByCompany.get("Blue Cross Blue Shield").size(), 3);
	}

	@DisplayName("Test that getting records from an improperly-formatted Enrollment file results in a BadFileFormatException")
	@Test
	void invalidEnrollmentFileFormat() {
		Assertions.assertThrows(BadFileFormatException.class,
				() -> {fileProcessorFacade.getCompanyRecordsFromFile(BROKEN_ENROLLMENT_FILE_PATH);});
	}

	@DisplayName("Test the writing of a single output file")
	@Test
	void writeOutputFilePerCompany() throws IOException {
		InsuranceEnrollmentRecord insuranceEnrollmentRecord = InsuranceEnrollmentRecord.newBuilder()
				.withUserId("ABC123XYZ")
				.withFullName("Sally Insured")
				.withVersion(2)
				.withInsuranceCompany("Test Company")
				.build();
		List<InsuranceEnrollmentRecord> testList = Collections.singletonList(insuranceEnrollmentRecord);
		Map<String, List<InsuranceEnrollmentRecord>> testMap = new HashMap<>();
		testMap.put("Test Company", testList);

		fileProcessorFacade.writeOutputFilePerCompany(OUTPUT_FILE_PATH, testMap);

		long fileCount = Files.list(Paths.get(OUTPUT_FILE_PATH))
				.filter(Files::isRegularFile).count();

		Assertions.assertTrue(fileCount > 0);
	}

	@DisplayName("Test reading and processing an input Enrollment file, and writing all expected output files")
	@Test
	void readInputFileAndProduceOutputFiles() throws IOException {
		//To delete any files in the outputFilePath directory before running this test, uncomment the below line
		//clearOutputDirectory(outputFilePath);

		Map<String, List<InsuranceEnrollmentRecord>> recordsByCompany =
				fileProcessorFacade.getCompanyRecordsFromFile(ENROLLMENT_FILE_PATH);
		Assertions.assertEquals(8, recordsByCompany.size());

		fileProcessorFacade.writeOutputFilePerCompany(OUTPUT_FILE_PATH, recordsByCompany);
		long fileCount = Files.list(Paths.get(OUTPUT_FILE_PATH))
				.filter(Files::isRegularFile).count();
		Assertions.assertEquals(fileCount, 8);
	}
}