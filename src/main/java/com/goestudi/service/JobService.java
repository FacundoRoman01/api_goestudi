package com.goestudi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goestudi.dto.JobDTO;
import com.goestudi.model.Job;
import com.goestudi.repository.JobRepository;

@Service
public class JobService {

	private JobRepository jobRepository;
	
	
	 @Autowired
	    public JobService(JobRepository jobRepository) {
	        this.jobRepository = jobRepository;
	    }

	    /**
	     * Nuevo método para obtener trabajos basados en múltiples filtros.
	     * @return Una lista de JobDTOs.
	     */
	    public List<JobDTO> findJobsByFilters(String keyword, List<String> location, Boolean isInternship, Boolean isPartTime) {
	        List<Job> jobs = jobRepository.findJobsByFilters(keyword, location, isInternship, isPartTime);
	        return jobs.stream()
	                .map(this::convertToDto)
	                .collect(Collectors.toList());
	    }

	    /**
	     * El método findAll que ya tenías, para referencia.
	     */
	    public List<JobDTO> findAll(String keyword) {
	        List<Job> jobs;
	        if (keyword != null && !keyword.isEmpty()) {
	            jobs = jobRepository.findByTitleContainingIgnoreCase(keyword);
	        } else {
	            jobs = jobRepository.findAll();
	        }
	        return jobs.stream()
	                .map(this::convertToDto)
	                .collect(Collectors.toList());
	    }
	    
	    // Método auxiliar para convertir una entidad Job a un JobDTO.
	    private JobDTO convertToDto(Job job) {
	        return new JobDTO(
	            job.getCompany(),
	            job.getLocation(),
	            job.getTitle(),
	            job.isPaid(),
	            job.getPostedAgo(),
	            job.getJobDetails(),
	            job.getIsInternship(), 
	            job.getIsPartTime()     
	        );
	    }
	
	
	
}
