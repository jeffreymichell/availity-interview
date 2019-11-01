package com.jeffreymichell.availityinterview.fileprocessor.domain;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord;

import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

class OutputFileWriter {
	private static final Logger log = getLogger(OutputFileWriter.class);
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	//Build file name from insurance company name and current date, replacing spaces with underscores
	private String buildFileName(String insuranceCompanyName) {
		String fileName = String.format(
				"%s_%s",
				insuranceCompanyName.replace(" ", "_"),
				LocalDate.now().format(dateFormatter));

		log.debug("Company Name [{}] = file name [{}]", insuranceCompanyName, fileName);
		return fileName;
	}

	//Generate output format (file/log) from InsuranceEnrollmentRecord
	String formatLineForOutput(InsuranceEnrollmentRecord insuranceEnrollmentRecord) {
		return String.format(
				"%s,%s,%d,%s",
				insuranceEnrollmentRecord.getUserId(),
				insuranceEnrollmentRecord.getFullName(),
				insuranceEnrollmentRecord.getVersion(),
				insuranceEnrollmentRecord.getInsuranceCompany());
	}

	//Writes an output file for a single Insurance Company
	void writeOutputFile(
			String outputDirectory,
			String insuranceCompanyName,
			List<InsuranceEnrollmentRecord> insuranceEnrollmentRecords) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(outputDirectory), "outputDirectory is required");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(insuranceCompanyName), "insuranceCompanyName is required");
		Preconditions.checkNotNull(insuranceEnrollmentRecords, "insuranceEnrollmentRecords is required");

		String filePathString = String.format("%s%s", outputDirectory, buildFileName(insuranceCompanyName));
		File out = new File(filePathString);
		try (PrintWriter printWriter = new PrintWriter(out)) {
			insuranceEnrollmentRecords
					.stream()
					.map(this::formatLineForOutput)
					.forEach(printWriter::println);
		} catch (FileNotFoundException e) {
			log.error("Error writing to output file: {}", filePathString, e);
		}
	}
}
