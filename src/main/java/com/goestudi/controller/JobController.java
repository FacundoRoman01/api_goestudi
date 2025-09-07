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
	    
	    
	    /**
	     * Endpoint para obtener todos los trabajos o filtrar por un término.
	     * GET /api/jobs?keyword=java
	     * @param keyword La palabra clave para la búsqueda.
	     * @return Una lista de objetos JobDTO.
	     */
	    @GetMapping
	    public List<JobDTO> getJobs(@RequestParam(required = false) String keyword) {
	        return jobService.findAll(keyword);
	    }
	
}
