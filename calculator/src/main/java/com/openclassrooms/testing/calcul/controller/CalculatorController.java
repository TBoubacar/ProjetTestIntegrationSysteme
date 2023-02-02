package com.openclassrooms.testing.calcul.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.openclassrooms.testing.calcul.domain.model.Calculation;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;
import com.openclassrooms.testing.calcul.service.CalculatorService;
import org.springframework.http.ResponseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

@Controller
public class CalculatorController {

	public static final String CALCULATOR_TEMPLATE = "calculator";
	private final Logger log = LogManager.getLogger(getClass());
	@Inject
	CalculatorService calculatorService;

	@GetMapping("/")
	public String index(Calculation calculation) {
		return "redirect:/calculator";
	}

	@GetMapping("/calculator")
	public String root(Calculation calculation) {
		return CALCULATOR_TEMPLATE; // cf. resources/templates/calculator.html
	}

	@PostMapping("/calculator")
	public String calculate(@Valid Calculation calculation, BindingResult bindingResult, Model model) {

		final CalculationType type = CalculationType.valueOf(calculation.getCalculationType());
		final CalculationModel calculationModel = new CalculationModel(
				type,
				calculation.getLeftArgument(),
				calculation.getRightArgument());

		final CalculationModel response = calculatorService.calculate(calculationModel);

		model.addAttribute("response", response);
		return CALCULATOR_TEMPLATE; // cf. resources/templates/calculator.html
	}
	
	// For UI Pages
	/*@ExceptionHandler(IllegalArgumentException.class)
	public String userNotFoundException(IllegalArgumentException ex) {
		log.info("Exception handled !");
		return "error";
	}*/
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
}
