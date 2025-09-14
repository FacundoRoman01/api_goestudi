package com.goestudi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goestudi.dto.JobDTO;
import com.goestudi.model.Job;
import com.goestudi.model.CompanyProfile;
import com.goestudi.repository.JobRepository;
import com.goestudi.specification.JobSpecification;
import com.goestudi.exception.JobNotFoundException;
import com.goestudi.model.Job.JobStatus;

import jakarta.validation.Valid;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Busca trabajos basados en filtros y los devuelve en un formato DTO paginado.
     */
    public Page<JobDTO> findJobsByFilters(
        String keyword,
        String location,
        Boolean isInternship,
        Boolean isPartTime,
        Pageable pageable) throws Exception {

        log.debug("SERVICE - Parámetros: keyword='{}', location='{}', isInternship={}, isPartTime={}",
                keyword, location, isInternship, isPartTime);

        try {
            Thread.sleep(1500); // Simulamos un retraso para la demostración

            Page<Job> jobPage = jobRepository.findAll(
                JobSpecification.findByFilters(keyword, location, isInternship, isPartTime),
                pageable
            );

            log.info("SERVICE - Trabajos encontrados en DB: {}", jobPage.getTotalElements());

            Page<JobDTO> dtoPage = jobPage.map(this::convertToDto);
            log.info("SERVICE - DTOs convertidos: {}", dtoPage.getContent().size());

            return dtoPage;

        } catch (Exception e) {
            log.error("ERROR en SERVICE: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Crea un nuevo trabajo basado en el DTO y el perfil de empresa proporcionado.
     */
    @Transactional
    public JobDTO createJob(CompanyProfile companyProfile, @Valid JobDTO jobDTO) {
        log.info("SERVICE - Creando un nuevo trabajo con título: {}", jobDTO.getTitle());

        Job job = new Job();
        job.setTitle(jobDTO.getTitle());
        job.setLocation(jobDTO.getLocation());
        job.setIsPaid(jobDTO.getIsPaid() != null ? jobDTO.getIsPaid() : false);
        job.setIsInternship(jobDTO.getIsInternship() != null ? jobDTO.getIsInternship() : false);
        job.setIsPartTime(jobDTO.getIsPartTime() != null ? jobDTO.getIsPartTime() : false);
        job.setDescription(jobDTO.getDescription());
        job.setJobDetails(jobDTO.getJobDetails());
        job.setRequirements(jobDTO.getRequirements());
        job.setSalary(jobDTO.getSalary());
        job.setPostedAt(LocalDateTime.now());
        job.setDeadline(jobDTO.getDeadline());
        job.setStatus(JobStatus.valueOf(jobDTO.getStatus()));

        // Relacionar con el CompanyProfile obtenido de forma segura
        job.setCompanyProfile(companyProfile);
        
        Job savedJob = jobRepository.save(job);
        log.info("SERVICE - Trabajo creado exitosamente con ID: {}", savedJob.getId());
        return convertToDto(savedJob);
    }

    /**
     * Actualiza un trabajo existente si el perfil de empresa coincide.
     */
    @Transactional
    public Optional<JobDTO> updateJob(CompanyProfile companyProfile, Long id, @Valid JobDTO jobDTO) {
        log.info("SERVICE - Actualizando trabajo con ID: {}", id);

        Optional<Job> jobOptional = jobRepository.findById(id);

        if (jobOptional.isPresent()) {
            Job existingJob = jobOptional.get();

            // Verificación de seguridad: solo la empresa propietaria puede actualizar el trabajo
            if (!existingJob.getCompanyProfile().getId().equals(companyProfile.getId())) {
                log.warn("SECURITY_ERROR - Intento de actualizar un trabajo no propio. ID trabajo: {}, ID empresa: {}", id, companyProfile.getId());
                return Optional.empty(); // No autorizado
            }

            existingJob.setTitle(jobDTO.getTitle());
            existingJob.setLocation(jobDTO.getLocation());
            existingJob.setIsPaid(jobDTO.getIsPaid() != null ? jobDTO.getIsPaid() : false);
            existingJob.setIsInternship(jobDTO.getIsInternship() != null ? jobDTO.getIsInternship() : false);
            existingJob.setIsPartTime(jobDTO.getIsPartTime() != null ? jobDTO.getIsPartTime() : false);
            existingJob.setDescription(jobDTO.getDescription());
            existingJob.setJobDetails(jobDTO.getJobDetails());
            existingJob.setRequirements(jobDTO.getRequirements());
            existingJob.setSalary(jobDTO.getSalary());
            existingJob.setDeadline(jobDTO.getDeadline());
            existingJob.setStatus(JobStatus.valueOf(jobDTO.getStatus()));

            Job updatedJob = jobRepository.save(existingJob);
            log.info("SERVICE - Trabajo actualizado exitosamente con ID: {}", updatedJob.getId());
            return Optional.of(convertToDto(updatedJob));
        }

        log.warn("SERVICE - Trabajo no encontrado con ID: {}", id);
        throw new JobNotFoundException("Job not found with ID: " + id);
    }

    /**
     * Elimina un trabajo si el perfil de empresa coincide.
     */
    @Transactional
    public boolean deleteJob(CompanyProfile companyProfile, Long id) {
        log.info("SERVICE - Intentando eliminar trabajo con ID: {}", id);
        
        Optional<Job> jobOptional = jobRepository.findById(id);
        
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            if (job.getCompanyProfile().getId().equals(companyProfile.getId())) {
                jobRepository.delete(job);
                log.info("SERVICE - Trabajo con ID: {} eliminado exitosamente.", id);
                return true;
            }
            log.warn("SECURITY_ERROR - Intento de eliminar un trabajo no propio. ID trabajo: {}, ID empresa: {}", id, companyProfile.getId());
            return false; // No autorizado
        }
        
        log.warn("SERVICE - Trabajo no encontrado para eliminar con ID: {}", id);
        return false;
    }
    
    /**
     * Convierte una entidad Job a un DTO.
     */
    public JobDTO convertToDto(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setLocation(job.getLocation());
        dto.setIsPaid(job.getIsPaid());
        dto.setIsInternship(job.getIsInternship());
        dto.setIsPartTime(job.getIsPartTime());
        dto.setDescription(job.getDescription());
        dto.setJobDetails(job.getJobDetails());
        dto.setRequirements(job.getRequirements());
        dto.setSalary(job.getSalary());
        dto.setPostedAt(job.getPostedAt());
        dto.setDeadline(job.getDeadline());
        dto.setStatus(job.getStatus().name());
        dto.setPostedAgo(job.getPostedAgo());
        if (job.getCompanyProfile() != null) {
            dto.setCompanyName(job.getCompanyProfile().getName());
        }
        return dto;
    }
}
