package com.jeffreymichell.availityinterview.lispchecker.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LispCheckerFacadeTest {
	private static final String PASSING_TEST_CASE_1 = "{stuff(thing[0])+{other[0-0]}}";
	private static final String PASSING_TEST_CASE_2 = "{[()]}(){}[]";
	private static final String PASSING_TEST_CASE_3 = "(a(b(c[1[2[3]]])))";
	private static final String FAILING_TEST_CASE_1 = "{oops(now}problems[]))";
	private static final String FAILING_TEST_CASE_2 = "({[]}))";
	private static final String FAILING_TEST_CASE_3 = "()(){[]}{";

	@Autowired
	LispCheckerFacade lispCheckerFacade;

	@DisplayName("Test LISP Checker returns true for valid LISP strings")
	@Test
	void doesLispStringParseSuccess() {
		//Positive test cases
		Assertions.assertTrue(lispCheckerFacade.doesLispStringParse(PASSING_TEST_CASE_1));
		Assertions.assertTrue(lispCheckerFacade.doesLispStringParse(PASSING_TEST_CASE_2));
		Assertions.assertTrue(lispCheckerFacade.doesLispStringParse(PASSING_TEST_CASE_3));
	}

	@DisplayName("Test LISP Checker returns false for invalid LISP strings")
	@Test
	void doesLispStringParseFailure() {
		//Negative test cases
		Assertions.assertFalse(lispCheckerFacade.doesLispStringParse(FAILING_TEST_CASE_1));
		Assertions.assertFalse(lispCheckerFacade.doesLispStringParse(FAILING_TEST_CASE_2));
		Assertions.assertFalse(lispCheckerFacade.doesLispStringParse(FAILING_TEST_CASE_3));
	}
}