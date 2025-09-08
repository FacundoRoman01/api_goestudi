package com.goestudi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goestudi.dto.JobDTO;
import com.goestudi.model.Job;
import com.goestudi.repository.JobRepository;
import com.goestudi.specification.JobSpecification;



@Service
public class JobService {

    private final JobRepository jobRepository;
    
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Page<JobDTO> findJobsByFilters(
        String keyword, 
        String location,
        Boolean isInternship, 
        Boolean isPartTime, 
        Pageable pageable) {
    	
    	   // --- INICIO DE LA SIMULACIÓN DE RETRASO ---
        try {
            Thread.sleep(3000); // Pausa la ejecución por 3 segundos
        } catch (InterruptedException e) {
            // Maneja la excepción si el hilo es interrumpido
            Thread.currentThread().interrupt(); 
        }
        // --- FIN DE LA SIMULACIÓN DE RETRASO ---
    	
        
        // Use the Specification to create the query with the filters
        Page<Job> jobPage = jobRepository.findAll(
            JobSpecification.findByFilters(keyword, location, isInternship, isPartTime),
            pageable
        );
        
        // Convert the page of entities to a page of DTOs
        return jobPage.map(this::convertToDto);
    }
    
    // Auxiliary method to convert a Job entity to a JobDTO.
    private JobDTO convertToDto(Job job) {
        return new JobDTO(
            job.getId(),
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