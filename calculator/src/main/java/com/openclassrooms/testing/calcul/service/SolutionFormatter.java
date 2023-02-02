package com.openclassrooms.testing.calcul.service;

import org.springframework.boot.test.mock.mockito.MockBean;

@MockBean
public interface SolutionFormatter {
	String format(int solution);
}
