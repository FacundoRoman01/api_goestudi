package com.goestudi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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

	    @Autowired
	    public JobController(JobService jobService) {
	        this.jobService = jobService;
	    }
	    
	    
	    @GetMapping
	    public List<JobDTO> getJobs(
	        @RequestParam(required = false) String keyword,
	        @RequestParam(required = false) List<String> location,
	        @RequestParam(required = false) Boolean isInternship, // Nuevo parámetro
	        @RequestParam(required = false) Boolean isPartTime   // Nuevo parámetro
	    ) {
	        // Llama al método de servicio que maneja todos los filtros
	        return jobService.findJobsByFilters(keyword, location, isInternship, isPartTime);
	    }
	
}
