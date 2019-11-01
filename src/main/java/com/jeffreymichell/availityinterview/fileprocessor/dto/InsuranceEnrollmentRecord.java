package com.jeffreymichell.availityinterview.fileprocessor.dto;

import com.google.common.base.MoreObjects;

public class InsuranceEnrollmentRecord {
	public static final class Builder {
		private String insuranceCompany;
		private int version;
		private String userId;
		private String fullName;
		private String firstName;
		private String lastName;

		private Builder() {
		}

		public Builder withInsuranceCompany(String val) {
			insuranceCompany = val;
			return this;
		}

		public Builder withVersion(int val) {
			version = val;
			return this;
		}

		public Builder withUserId(String val) {
			userId = val;
			return this;
		}

		public Builder withFullName(String val) {
			fullName = val;
			String[] nameParts = fullName.split(" ");
			firstName = MoreObjects.firstNonNull(nameParts[0], "");
			lastName = MoreObjects.firstNonNull(nameParts[nameParts.length - 1], "");
			return this;
		}

		public InsuranceEnrollmentRecord build() {
			return new InsuranceEnrollmentRecord(this);
		}
	}

	private InsuranceEnrollmentRecord(Builder builder) {
		insuranceCompany = builder.insuranceCompany;
		version = builder.version;
		userId = builder.userId;
		fullName = builder.fullName;
		firstName = builder.firstName;
		lastName = builder.lastName;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static Builder newBuilder(InsuranceEnrollmentRecord copy) {
		Builder builder = new Builder();
		builder.insuranceCompany = copy.getInsuranceCompany();
		builder.version = copy.getVersion();
		builder.userId = copy.getUserId();
		builder.fullName = copy.getFullName();
		return builder;
	}

	private final String insuranceCompany;
	private final int version;
	private final String userId;
	private final String fullName;
	private final String firstName;
	private final String lastName;


	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public int getVersion() {
		return version;
	}

	public String getUserId() {
		return userId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("insuranceCompany", insuranceCompany)
				.add("version", version)
				.add("userId", userId)
				.add("fullName", fullName)
				.add("firstName", firstName)
				.add("lastName", lastName)
				.toString();
	}
}
