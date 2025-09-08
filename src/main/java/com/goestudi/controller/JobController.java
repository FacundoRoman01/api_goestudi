package com.goestudi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goestudi.dto.JobDTO;
import com.goestudi.service.JobService;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;
    
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    
    @GetMapping
    public ResponseEntity<Page<JobDTO>> getJobs(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String location, // Changed to String
        @RequestParam(required = false) Boolean isInternship,
        @RequestParam(required = false) Boolean isPartTime,
        @PageableDefault(size = 12) Pageable pageable) {
        
        Page<JobDTO> jobPage = jobService.findJobsByFilters(keyword, location, isInternship, isPartTime, pageable);
        
        return ResponseEntity.ok(jobPage);
    }
}