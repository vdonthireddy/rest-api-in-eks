package com.niharsystems.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@GetMapping("/hi")
	public String greeting() {
		return new String( String.format("Hello, Don!!"));
	}
}
