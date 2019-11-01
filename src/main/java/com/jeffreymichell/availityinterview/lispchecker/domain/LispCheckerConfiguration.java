package com.jeffreymichell.availityinterview.lispchecker.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LispCheckerConfiguration {
	@Bean
	LispCheckerFacade lispCheckerFacade() {
		LispCodeValidator lispCodeValidator = new LispCodeValidator();
		return new LispCheckerFacade(lispCodeValidator);
	}
}
