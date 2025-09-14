package com.goestudi.service;

import com.goestudi.dto.CompanyProfileDTO;
import com.goestudi.dto.JobDTO;
import com.goestudi.model.CompanyProfile;
import com.goestudi.model.Job;
import com.goestudi.model.User;
import com.goestudi.repository.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;

    @Autowired
    public CompanyProfileService(CompanyProfileRepository companyProfileRepository) {
        this.companyProfileRepository = companyProfileRepository;
    }

    public Optional<CompanyProfile> findByUser(User user) {
        return companyProfileRepository.findByUser(user);
    }

    @Transactional
    public CompanyProfileDTO createOrUpdateProfile(User user, CompanyProfileDTO companyProfileDTO) {
        Optional<CompanyProfile> existingProfile = companyProfileRepository.findByUser(user);

        CompanyProfile profile;
        if (existingProfile.isPresent()) {
            profile = existingProfile.get();
        } else {
            profile = new CompanyProfile();
            profile.setUser(user);
        }

        profile.setName(companyProfileDTO.getName());
        profile.setDescription(companyProfileDTO.getDescription());
        profile.setLocation(companyProfileDTO.getLocation());
        profile.setWebsite(companyProfileDTO.getWebsite());
        profile.setIndustry(companyProfileDTO.getIndustry());
        profile.setLogoUrl(companyProfileDTO.getLogoUrl());
        profile.setEmployeeCount(companyProfileDTO.getEmployeeCount());
        profile.setFoundedYear(companyProfileDTO.getFoundedYear());

        CompanyProfile savedProfile = companyProfileRepository.save(profile);
        return convertToDto(savedProfile);
    }

    public CompanyProfileDTO convertToDto(CompanyProfile companyProfile) {
        CompanyProfileDTO dto = new CompanyProfileDTO();
        dto.setId(companyProfile.getId());
        dto.setName(companyProfile.getName());
        dto.setDescription(companyProfile.getDescription());
        dto.setLocation(companyProfile.getLocation());
        dto.setWebsite(companyProfile.getWebsite());
        dto.setIndustry(companyProfile.getIndustry());
        dto.setLogoUrl(companyProfile.getLogoUrl());
        dto.setEmployeeCount(companyProfile.getEmployeeCount());
        dto.setFoundedYear(companyProfile.getFoundedYear());
        dto.setCreatedAt(companyProfile.getCreatedAt());
        dto.setUpdatedAt(companyProfile.getUpdatedAt());
        dto.setIsVerified(companyProfile.getIsVerified());

        // Convertir jobs a DTOs
        List<JobDTO> jobs = companyProfile.getJobs() != null
                ? companyProfile.getJobs().stream().map(job -> {
                    JobDTO jdto = new JobDTO();
                    jdto.setId(job.getId());
                    jdto.setTitle(job.getTitle());
                    jdto.setDescription(job.getDescription());
                    jdto.setLocation(job.getLocation());
                    jdto.setSalary(job.getSalary());
                    jdto.setStatus(job.getStatus() != null ? job.getStatus().name() : null); // <-- Aquí está la corrección
                    jdto.setIsPaid(job.getIsPaid());
                    jdto.setIsInternship(job.getIsInternship());
                    jdto.setIsPartTime(job.getIsPartTime());
                    jdto.setDeadline(job.getDeadline());
                    return jdto;
                }).collect(Collectors.toList())
                : List.of();

        dto.setJobs(jobs);

        return dto;
    }

}
