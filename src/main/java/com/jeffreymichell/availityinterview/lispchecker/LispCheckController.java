package com.jeffreymichell.availityinterview.lispchecker;

import com.jeffreymichell.availityinterview.lispchecker.domain.LispCheckerFacade;
import com.jeffreymichell.availityinterview.lispchecker.dto.LispCodeCheckRequest;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
public class LispCheckController {
	private static final Logger log = getLogger(LispCheckController.class);
	private static final String LISP_CODE_VALID = "The LISP code passed validation";
	private static final String LISP_CODE_INVALID = "The LISP code failed validation";
	private LispCheckerFacade lispCheckerFacade;

	public LispCheckController(LispCheckerFacade lispCheckerFacade) {
		this.lispCheckerFacade = lispCheckerFacade;
	}

	@PostMapping("validateLispCode")
	@ResponseBody
	ResponseEntity<Object> validateLispCode(@RequestBody LispCodeCheckRequest request) {
		log.info("validateLispCode() invoked with {}", request);
		boolean result = lispCheckerFacade.doesLispStringParse(request.getLispCode());
		if (result) {
			return new ResponseEntity<>(LISP_CODE_VALID, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(LISP_CODE_INVALID, HttpStatus.OK);
		}
	}
}
