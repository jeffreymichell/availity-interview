package com.jeffreymichell.availityinterview;

import com.jeffreymichell.availityinterview.fileprocessor.ProcessFileController;
import com.jeffreymichell.availityinterview.lispchecker.LispCheckController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AvailityInterviewApplicationTests {
	@Autowired
	private LispCheckController lispCheckController;

	@Autowired
	private ProcessFileController processFileController;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(lispCheckController);
		Assertions.assertNotNull(processFileController);
	}

}
