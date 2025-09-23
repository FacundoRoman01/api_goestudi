package com.goestudi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.goestudi.model.CompanyProfile;
import com.goestudi.model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>  {
	
	 /**
     * Busca todos los trabajos asociados a un perfil de empresa específico.
     * @param companyProfile El perfil de empresa por el que se debe filtrar.
     * @param pageable Objeto de paginación para limitar y ordenar los resultados.
     * @return Una página de objetos Job.
     */
    Page<Job> findByCompanyProfile(CompanyProfile companyProfile, Pageable pageable);
    
    List<Job> findByCompanyProfile(CompanyProfile companyProfile);
    
    
    /*@Query(value = """
    	    SELECT DISTINCT LOWER(SUBSTRING_INDEX(j.title, ' ', 1)) AS keyword
    	    FROM jobs j
    	    WHERE LOWER(j.title) LIKE CONCAT('%', :keyword, '%')
    	    LIMIT 15
    	    """, nativeQuery = true)
    	List<String> findTitleFirstWordsByKeyword(@Param("keyword") String keyword);*/

    


}
