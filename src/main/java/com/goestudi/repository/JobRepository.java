package com.goestudi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goestudi.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	
	 // Método original para buscar por palabra clave en el título
    List<Job> findByTitleContainingIgnoreCase(String keyword);

    // Nuevo método para buscar por múltiples filtros
    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:isInternship IS NULL OR j.isInternship = :isInternship) AND " +
           "(:isPartTime IS NULL OR j.isPartTime = :isPartTime)")
    List<Job> findJobsByFilters(
        @Param("keyword") String keyword,
        @Param("location") List<String> location,
        @Param("isInternship") Boolean isInternship,
        @Param("isPartTime") Boolean isPartTime
    );

}
