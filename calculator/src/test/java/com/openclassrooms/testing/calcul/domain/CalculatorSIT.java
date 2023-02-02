package com.openclassrooms.testing.calcul.domain;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith( SpringExtension.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class CalculatorSIT {
	
	@Autowired
	private ApplicationContext context;
	
	@Test
	public void testAddTwoPositiveNumbers() {
		int res = context.getBean( Calculator.class ).add(3, 4);
		assertEquals(res, 7);
	}
	

}
