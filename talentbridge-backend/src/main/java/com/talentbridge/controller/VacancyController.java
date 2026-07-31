package com.talentbridge.controller;

import com.talentbridge.dto.request.CreateVacancyRequest;
import com.talentbridge.dto.request.UpdateVacancyRequest;
import com.talentbridge.dto.response.VacancyResponse;
import com.talentbridge.service.VacancyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {


    private final VacancyService vacancyService;


    @PostMapping
    public ResponseEntity<VacancyResponse> createVacancy(
            @Valid @RequestBody CreateVacancyRequest request) {

        return new ResponseEntity<>(
                vacancyService.createVacancy(request),
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<List<VacancyResponse>> getAllVacancies() {

        return ResponseEntity.ok(
                vacancyService.getAllVacancies()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponse> getVacancyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                vacancyService.getVacancyById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<VacancyResponse> updateVacancy(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVacancyRequest request) {

        return ResponseEntity.ok(
                vacancyService.updateVacancy(id, request)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(
            @PathVariable Long id) {

        vacancyService.deleteVacancy(id);

        return ResponseEntity.noContent().build();
    }
}