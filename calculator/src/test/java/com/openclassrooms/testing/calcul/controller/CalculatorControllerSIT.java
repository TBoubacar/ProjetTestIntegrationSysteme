package com.openclassrooms.testing.calcul.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith ( SpringExtension.class )
@SpringBootTest ( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@AutoConfigureMockMvc
public class CalculatorControllerSIT {

	@Inject
	private MockMvc mockMvc;

	
	@Test
	public void givenACalculatorApp_whenRequestToAdd_thenSolutionIsShown() throws Exception {
		final MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/calculator")
						.param("leftArgument", "2")
						.param("rightArgument", "3")
						.param("calculationType", "ADDITION"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andReturn();

		assertThat(result.getResponse().getContentAsString())
				.contains("id=\"solution\"")
				.contains(">5</span");
	}

	@Test
	public void givenACalculatorApp_whenRequestToDiv_thenSolutionIsShown() throws Exception {
		final MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/calculator")
						.param("leftArgument", "2")
						.param("rightArgument", "0")
						.param("calculationType", "DIVISION"))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError()) 
				.andReturn();

		assertThat(result.getResponse().getContentAsString())
				.contains("java.lang.ArithmeticException: / by zero");
	}
	
}
