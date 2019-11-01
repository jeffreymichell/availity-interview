package com.jeffreymichell.availityinterview.fileprocessor.domain;

import com.jeffreymichell.availityinterview.fileprocessor.dto.InsuranceEnrollmentRecord;

import java.util.Comparator;

class InsuranceEnrollmentRecordComparators {
	static Comparator<InsuranceEnrollmentRecord> compareByFirstName = Comparator.comparing(InsuranceEnrollmentRecord::getFirstName);

	static Comparator<InsuranceEnrollmentRecord> compareByLastName = Comparator.comparing(InsuranceEnrollmentRecord::getLastName);

	static Comparator<InsuranceEnrollmentRecord> compareByNameLastThenFirst = compareByLastName.thenComparing(compareByFirstName);

	static Comparator<InsuranceEnrollmentRecord> compareByVersion = Comparator.comparing(InsuranceEnrollmentRecord::getVersion);

	static Comparator<InsuranceEnrollmentRecord> compareByUserId = Comparator.comparing(InsuranceEnrollmentRecord::getUserId);

	static Comparator<InsuranceEnrollmentRecord> compareByUserIdAndDescendingVersion =
			compareByUserId.thenComparing(compareByVersion.reversed());
}
