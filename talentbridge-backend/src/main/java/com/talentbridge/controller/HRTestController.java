package com.talentbridge.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr")
public class HRTestController {

	@GetMapping("/test")
	public String hrTest() {

		return "HR ACCESS GRANTED";
	}

}