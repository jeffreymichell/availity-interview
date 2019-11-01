package com.jeffreymichell.availityinterview.lispchecker.dto;

import com.google.common.base.MoreObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(builder = LispCodeCheckRequest.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LispCodeCheckRequest implements Serializable {
	private static final long serialVersionUID = 8298127263620334737L;

	public static Builder newBuilder(LispCodeCheckRequest copy) {
		Builder builder = new Builder();
		builder.lispCode = copy.getLispCode();
		return builder;
	}

	public static final class Builder {
		private String lispCode;

		private Builder() {
		}

		public Builder withLispCode(String val) {
			lispCode = val;
			return this;
		}

		public LispCodeCheckRequest build() {
			return new LispCodeCheckRequest(this);
		}
	}

	private LispCodeCheckRequest(Builder builder) {
		lispCode = builder.lispCode;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	private final String lispCode;

	public String getLispCode() {
		return lispCode;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("lispCode", lispCode)
				.toString();
	}
}
