package com.talentbridge.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate")
public class CandidateTestController {

	@GetMapping("/test")
	public String candidateTest() {

		return "CANDIDATE ACCESS GRANTED";
	}

}