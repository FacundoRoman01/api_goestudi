package com.goestudi.specification;

import com.goestudi.model.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    public static Specification<Job> findByFilters(
        String keyword,
        String location, // Changed to String to match your request
        Boolean isInternship,
        Boolean isPartTime) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by keyword in title or company
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likeKeyword);
                Predicate companyPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("company")), likeKeyword);
                predicates.add(criteriaBuilder.or(titlePredicate, companyPredicate));
            }

            // Filter by location
            if (location != null && !location.isEmpty()) {
                String likeLocation = "%" + location.toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), likeLocation));
            }
            
            // Filter by job type (isInternship)
            if (isInternship != null) {
                predicates.add(criteriaBuilder.equal(root.get("isInternship"), isInternship));
            }
            
            // Filter by job type (isPartTime)
            if (isPartTime != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPartTime"), isPartTime));
            }

            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}