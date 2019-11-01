package com.jeffreymichell.availityinterview.fileprocessor.dto;

import com.google.common.base.MoreObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(builder = EnrollmentFileRequest.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrollmentFileRequest implements Serializable {
	private static final long serialVersionUID = 2024179234377753301L;

	public static final class Builder {
		private String enrollmentFilePath;
		private String outputfilePath;

		private Builder() {
		}

		public Builder withEnrollmentFilePath(String val) {
			enrollmentFilePath = val;
			return this;
		}

		public Builder withOutputfilePath(String val) {
			outputfilePath = val;
			return this;
		}

		public EnrollmentFileRequest build() {
			return new EnrollmentFileRequest(this);
		}
	}

	private EnrollmentFileRequest(Builder builder) {
		enrollmentFilePath = builder.enrollmentFilePath;
		outputfilePath = builder.outputfilePath;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static Builder newBuilder(EnrollmentFileRequest copy) {
		Builder builder = new Builder();
		builder.enrollmentFilePath = copy.getEnrollmentFilePath();
		builder.outputfilePath = copy.getOutputfilePath();
		return builder;
	}


	private final String enrollmentFilePath;
	private final String outputfilePath;

	public String getEnrollmentFilePath() {
		return enrollmentFilePath;
	}

	public String getOutputfilePath() {
		return outputfilePath;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("enrollmentFilePath", enrollmentFilePath)
				.add("outputfilePath", outputfilePath)
				.toString();
	}
}
