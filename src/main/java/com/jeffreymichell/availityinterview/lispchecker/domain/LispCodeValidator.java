package com.jeffreymichell.availityinterview.lispchecker.domain;

import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

class LispCodeValidator {
	private static final Logger log = getLogger(LispCodeValidator.class);
	private static final Set<Character> OPENERS = new HashSet<>(Arrays.asList('(', '{', '['));
	private static final Set<Character> CLOSERS = new HashSet<>(Arrays.asList(')', '}', ']'));
	private static final Tuple2<Character, Character> BRACKET_PAIR = new Tuple2<>('[', ']');
	private static final Tuple2<Character, Character> CURLY_PAIR = new Tuple2<>('{', '}');
	private static final Tuple2<Character, Character> PAREN_PAIR = new Tuple2<>('(', ')');

	//Return true for empty strings. Return true if all opening parentheses are properly closed, else return false
	boolean isParenthesesNestingValid(String lispCode) {
		if (lispCode.isEmpty()) {
			return true;
		}

		Deque<Character> charStack = new ArrayDeque<>(lispCode.length());

		for (char candidate : lispCode.toCharArray()) {
			if (OPENERS.contains(candidate)) {
				charStack.push(candidate);
			} else if (CLOSERS.contains(candidate)) {
				if (!isMatchingClose(charStack, candidate)) {
					return false;
				}
			}
		}

		return charStack.isEmpty();
	}

	//Check if candidate char is the closing char for the top of the stack
	private boolean isMatchingClose(Deque<Character> stack, char candidate) {
		if (stack.isEmpty()) {
			return false;
		}
		char lastOpener = stack.pop();
		if (PAREN_PAIR.v1.equals(lastOpener)) {
			return PAREN_PAIR.v2.equals(candidate);
		} else if (BRACKET_PAIR.v1.equals(lastOpener)) {
			return BRACKET_PAIR.v2.equals(candidate);
		} else if (CURLY_PAIR.v1.equals(lastOpener)) {
			return CURLY_PAIR.v2.equals(candidate);
		} else {
			log.error("isMatchingClose() invoked with improper value: {}", candidate);
			return false;
		}
	}
}
