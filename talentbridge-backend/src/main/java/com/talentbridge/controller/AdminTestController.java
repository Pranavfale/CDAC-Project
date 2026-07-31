package com.talentbridge.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminTestController {

	@GetMapping("/test")
	public String adminTest() {

		return "ADMIN ACCESS GRANTED";
	}

}