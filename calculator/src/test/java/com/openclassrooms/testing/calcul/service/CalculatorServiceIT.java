package com.openclassrooms.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {CalculatorServiceIT.class, Calculator.class, SolutionFormatter.class, CalculatorService.class })
public class CalculatorServiceIT {

    @MockBean
	private Calculator calculator;

    @MockBean
    private SolutionFormatter formatter;

    @Autowired
	private CalculatorService underTest;

	@Test
	public void calculatorService_shouldCalculateASolution_whenGivenACalculationModel() {
		// GIVEN
		final CalculationModel calculation = new CalculationModel(CalculationType.ADDITION, 100, 101);
        when(calculator.add(100, 101)).thenReturn(201);
        when(formatter.format(201)).thenReturn("201");
		
		// WHEN
		final CalculationModel result = underTest.calculate(calculation);

        // THEN
        assertThat(result.getSolution()).isEqualTo(201);
        verify(calculator, times(1)).add(100, 101);
        verify(formatter, times(1)).format(201);
	}
}