package com.jeffreymichell.availityinterview.fileprocessor.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class is the public API for all functionality in the fileprocessor domain
 */
public class FileProcessorFacade {
	private static final Logger log = getLogger(FileProcessorFacade.class);
	private EnrollmentFileExtractor enrollmentFileExtractor;
	private OutputFileWriter outputFileWriter;

	FileProcessorFacade(EnrollmentFileExtractor enrollmentFileExtractor, OutputFileWriter outputFileWriter) {
		this.enrollmentFileExtractor = enrollmentFileExtractor;
		this.outputFileWriter = outputFileWriter;
	}

	/**
	 *
	 * @param pathToInputFile the String representation of the path to the input file, including the file name
	 * @return a map of Insurance Company names to lists of {@link com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord}
	 * @throws IOException
	 */
	public Map<String, List<InsuranceEnrollmentRecord>> getCompanyRecordsFromFile(String pathToInputFile) throws IOException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(pathToInputFile), "pathToInputFile is required");
		log.debug("About to parse Company records from file: {}", pathToInputFile);
		try {
			Map<String, List<InsuranceEnrollmentRecord>> recordsGroupedByCompany =
					enrollmentFileExtractor.getRecordsFromInputFile(pathToInputFile);

			Map<String, List<InsuranceEnrollmentRecord>> uniqueOrderedRecords = recordsGroupedByCompany
					.entrySet()
					.stream()
					.map(item -> new AbstractMap.SimpleEntry<>(item.getKey(),
							enrollmentFileExtractor.deDuplicateAndSort(item.getValue())))
					.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

			uniqueOrderedRecords.forEach((key, value) -> log.debug("{}: {}", key, formatForLogOutput(value)));
			return uniqueOrderedRecords;
		} catch (IOException e) {
			log.error("Exception parsing file: {}", pathToInputFile, e);
			throw e;
		}
	}

	//Convenience method - formats List<InsuranceEnrollmentRecord> for log output
	private String formatForLogOutput(List<InsuranceEnrollmentRecord> informationList) {
		return informationList
				.stream()
				.map(item -> outputFileWriter.formatLineForOutput(item))
				.collect(Collectors.joining("\n"));
	}

	/**
	 *
	 * @param outputDirectoryPath The string representation of the path to the directory where output files should be written
	 * @param recordsGroupedByCompany a map of Insurance Company names to lists of
	 *                            {@link com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord}
	 */
	public void writeOutputFilePerCompany(
			String outputDirectoryPath,
			Map<String, List<InsuranceEnrollmentRecord>> recordsGroupedByCompany) {
		log.debug("Writing {} output files", recordsGroupedByCompany.size());
		recordsGroupedByCompany.forEach((key, value) -> outputFileWriter.writeOutputFile(outputDirectoryPath, key, value));
	}
}
