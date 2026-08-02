package com.talentbridge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentbridge.dto.request.ScheduleInterviewRequest;
import com.talentbridge.dto.request.UpdateInterviewStatusRequest;
import com.talentbridge.dto.response.InterviewResponse;
import com.talentbridge.service.InterviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

	private final InterviewService interviewService;

	// HR schedules interview
	@PostMapping
	public ResponseEntity<InterviewResponse> scheduleInterview(@RequestBody ScheduleInterviewRequest request) {

		return new ResponseEntity<>(interviewService.scheduleInterview(request), HttpStatus.CREATED);
	}

	// View interview by application
	@GetMapping("/application/{applicationId}")
	public ResponseEntity<List<InterviewResponse>> getApplicationInterview(@PathVariable Long applicationId,
			Authentication authentication) {

		return ResponseEntity.ok(interviewService.getApplicationInterview(applicationId, authentication.getName()));
	}

	// Update interview status
	@PutMapping("/{interviewId}/status")
	public ResponseEntity<Void> updateInterviewStatus(@PathVariable Long interviewId,
			@RequestBody UpdateInterviewStatusRequest request) {

		interviewService.updateInterviewStatus(interviewId, request);

		return ResponseEntity.ok().build();
	}

}