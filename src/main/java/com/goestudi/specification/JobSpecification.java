package com.goestudi.specification;

import com.goestudi.model.Job;
import com.goestudi.model.Location;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    /**
     * Crea una Specification que permite filtrar trabajos por múltiples criterios
     * 
     * Funcionamiento:
     * 1. KEYWORD: Busca en título, nombre de empresa Y descripción (usando OR)
     * 2. LOCATION: Busca coincidencias parciales en ubicación
     * 3. TYPE FILTERS: Filtra por tipo de trabajo (pasantía, medio tiempo)
     * 4. Todos los filtros se combinan con AND (deben cumplirse todos)
     */
	public static Specification<Job> findByFilters(
		    String keyword,
		    Location location,
		    Boolean isInternship,
		    Boolean isPartTime) {

		    return (root, query, criteriaBuilder) -> {
		        List<Predicate> predicates = new ArrayList<>();

		        if (keyword != null && !keyword.isEmpty()) {
		            String likeKeyword = "%" + keyword.toLowerCase() + "%";

		            Predicate titlePredicate = criteriaBuilder.like(
		                criteriaBuilder.lower(root.get("title")),
		                likeKeyword
		            );

		            // join con companyProfile como LEFT JOIN
		            Predicate companyPredicate = criteriaBuilder.like(
		                criteriaBuilder.lower(root.join("companyProfile", JoinType.LEFT).get("name")),
		                likeKeyword
		            );

		            Predicate descriptionPredicate = criteriaBuilder.like(
		                criteriaBuilder.lower(root.get("description")),
		                likeKeyword
		            );

		            Predicate requirementsPredicate = criteriaBuilder.like(
		                criteriaBuilder.lower(root.get("requirements")),
		                likeKeyword
		            );

		            predicates.add(criteriaBuilder.or(
		                titlePredicate,
		                companyPredicate,
		                descriptionPredicate,
		                requirementsPredicate
		            ));
		        }

		        if (location != null) {
		            predicates.add(criteriaBuilder.equal(root.get("location"), location));
		        }

		        if (isInternship != null) {
		            predicates.add(criteriaBuilder.equal(root.get("isInternship"), isInternship));
		        }

		        if (isPartTime != null) {
		            predicates.add(criteriaBuilder.equal(root.get("isPartTime"), isPartTime));
		        }

		        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		    };
		}
    
    /**
     * Specification simplificada solo para búsqueda por palabra clave
     * Útil para testing y debugging
     */
    public static Specification<Job> findByKeywordOnly(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.equal(root.get("status"), Job.JobStatus.ACTIVE);
            }
            
            String likeKeyword = "%" + keyword.trim().toLowerCase() + "%";
            
            Predicate titlePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")), likeKeyword);
            Predicate companyPredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("companyProfile").get("name")), likeKeyword);
            Predicate descriptionPredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("description")), likeKeyword);
                
            List<Predicate> keywordPredicates = new ArrayList<>();
            keywordPredicates.add(titlePredicate);
            keywordPredicates.add(descriptionPredicate);
            
            // Solo buscar en company si no es null
            Predicate companyNotNullAndMatch = criteriaBuilder.and(
                criteriaBuilder.isNotNull(root.get("companyProfile")),
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("companyProfile").get("name")), 
                    likeKeyword)
            );
            keywordPredicates.add(companyNotNullAndMatch);
            
            Predicate keywordMatch = criteriaBuilder.or(keywordPredicates.toArray(new Predicate[0]));
            Predicate activeStatus = criteriaBuilder.equal(root.get("status"), Job.JobStatus.ACTIVE);
            
            return criteriaBuilder.and(keywordMatch, activeStatus);
        };
    }
    
    /**
     * Specification que retorna todos los trabajos activos
     * Útil cuando no hay filtros aplicados
     */
    public static Specification<Job> findActiveJobs() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("status"), Job.JobStatus.ACTIVE);
    }
}