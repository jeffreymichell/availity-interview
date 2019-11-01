package com.jeffreymichell.availityinterview.fileprocessor.domain;

import com.jeffreymichell.availityinterview.fileprocessor.dto.BadFileFormatException;
import com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.jeffreymichell.availityinterview.fileprocessor.domain.InsuranceEnrollmentRecordComparators.compareByNameLastThenFirst;
import static com.jeffreymichell.availityinterview.fileprocessor.domain.InsuranceEnrollmentRecordComparators.compareByUserIdAndDescendingVersion;
import static java.util.stream.Collectors.groupingBy;
import static org.slf4j.LoggerFactory.getLogger;

class EnrollmentFileExtractor {
	private static final Logger log = getLogger(EnrollmentFileExtractor.class);

	//Stateful filter to return distinct records by UserId
	private static <T> Predicate<T> distinctEnrollmentRecordByUserId(
			Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	//Sort the records for a single Insurance Company, putting the max version for each UserId first,
	// then filter for distinct UserIds,
	// then sort by lastName, firstName
	List<InsuranceEnrollmentRecord> deDuplicateAndSort(List<InsuranceEnrollmentRecord> rawList) {
		return rawList.stream()
				.sorted(compareByUserIdAndDescendingVersion)
				.filter(distinctEnrollmentRecordByUserId(InsuranceEnrollmentRecord::getUserId))
				.sorted(compareByNameLastThenFirst)
				.collect(Collectors.toList());
	}

	//Get all records from input file, map each to InsuranceEnrollmentRecord object, then group by Insurance Company
	Map<String, List<InsuranceEnrollmentRecord>> getRecordsFromInputFile(String pathToInputFile) throws IOException {
		return Files
				.lines(Paths.get(pathToInputFile))
				.map(this::mapRecord)
				.collect(groupingBy(InsuranceEnrollmentRecord::getInsuranceCompany));
	}

	//Map a single line from the input file to an InsuranceEnrollmentRecord object
	private InsuranceEnrollmentRecord mapRecord(String record) {
		String[] parts = record.split(",");
		if (parts.length != 4) {
			log.error("Enrollment file record had unexpected format: {}", record);
			throw new BadFileFormatException(record);
		}
		try {
			InsuranceEnrollmentRecord insuranceEnrollmentRecord = InsuranceEnrollmentRecord.newBuilder()
					.withUserId(parts[0].trim())
					.withFullName(parts[1].trim())
					.withVersion(Integer.parseInt(parts[2].trim()))
					.withInsuranceCompany(parts[3].trim())
					.build();
			log.debug("Parsed Record: {}", insuranceEnrollmentRecord);
			return insuranceEnrollmentRecord;
		} catch (Exception e) {
			log.error("InsuranceEnrollmentRecord could not be built from [{}]", record, e);
			throw new BadFileFormatException(record);
		}
	}
}
