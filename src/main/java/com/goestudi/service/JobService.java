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
     * Obtiene todos los trabajos o los filtra por una palabra clave.
     * Convierte las entidades Job a DTOs.
     * @param keyword El término de búsqueda opcional.
     * @return Una lista de JobDTOs.
     */
    public List<JobDTO> findAll(String keyword) {
        List<Job> jobs;
        if (keyword != null && !keyword.isEmpty()) {
            jobs = jobRepository.findByTitleContainingIgnoreCase(keyword);
        } else {
            jobs = jobRepository.findAll();
        }
        // Convierte cada entidad Job a un JobDTO
        return jobs.stream()
                   .map(this::convertToDto)
                   .collect(Collectors.toList());
    }
    
    /**
     * Método auxiliar para convertir una entidad Job a un JobDTO.
     * @param job La entidad Job.
     * @return El JobDTO resultante.
     */
    private JobDTO convertToDto(Job job) {
        return new JobDTO(
            job.getCompany(),
            job.getLocation(),
            job.getTitle(),
            job.isPaid(),
            job.getPostedAgo(),
            job.getJobDetails()
        );
    }
	
	
	
	
	
	
	
	
}
