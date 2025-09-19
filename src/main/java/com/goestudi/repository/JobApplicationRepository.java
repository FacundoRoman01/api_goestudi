package com.goestudi.repository;

import com.goestudi.model.Job;
import com.goestudi.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUserProfileId(Long userId);
    List<JobApplication> findByJobId(Long jobId);
    
    List<JobApplication> findByJobIn(List<Job> jobs);
}
