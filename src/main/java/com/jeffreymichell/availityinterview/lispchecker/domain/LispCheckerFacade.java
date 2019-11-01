package com.jeffreymichell.availityinterview.lispchecker.domain;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class is the public API for all functionality in the lispchecker domain
 */
public class LispCheckerFacade {
	private static final Logger log = getLogger(LispCheckerFacade.class);
	private LispCodeValidator lispCodeValidator;

	LispCheckerFacade(LispCodeValidator lispCodeValidator) {
		this.lispCodeValidator = lispCodeValidator;
	}

	/**
	 *
	 * @param lispCode The String containing the LISP code to validate, must not be null
	 * @return true if the LISP code has valid parentheses nesting, or else false
	 */
	public boolean doesLispStringParse(String lispCode) {
		Preconditions.checkNotNull(lispCode, "lispCode cannot be null");
		log.debug("About to parse LISP code String: {}", lispCode);
		return lispCodeValidator.isParenthesesNestingValid(lispCode);
	}
}
