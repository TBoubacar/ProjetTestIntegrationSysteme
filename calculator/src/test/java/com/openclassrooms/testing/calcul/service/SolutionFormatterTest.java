package com.openclassrooms.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.openclassrooms.testing.calcul.domain.LoggingExtension;

@ExtendWith(LoggingExtension.class)
public class SolutionFormatterTest {

	private SolutionFormatter solutionFormatter;

	private Logger logger;

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	@BeforeEach
	public void initFormatter() {
		solutionFormatter = new SolutionFormatterImpl();
	}

	@Test
	@Disabled
	public void format_shouldFormatAnyBigNumber() {
		// GIVEN
		final int number = 1234567890;

		// WHEN
		final String result = solutionFormatter.format(number);

		//logger.info("result="+result);
		
		// THEN
		assertThat(result).isEqualTo("1 234 567 890");
	}

}
