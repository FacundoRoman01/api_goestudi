// src/main/java/com/goestudi/model/Job.java

package com.goestudi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Job {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 
	 private String company;
	 private String location;
	 private String title;
	 
	 // Mapea el campo 'isPaid' a la columna 'is_paid' de la base de datos
	 @Column(name = "is_paid")
	 private boolean isPaid;
	 
	 // Mapea el campo 'postedAgo' a la columna 'posted_ago'
	 @Column(name = "posted_ago")
	 private String postedAgo;
	 
	 // Mapea el campo 'jobDetails' a la columna 'job_details'
	 @Column(name = "job_details")
	 private String jobDetails;
	
	  // Constructor
    public Job() {}

    
    public Job(String company, String location, String title, boolean isPaid, String postedAgo, String jobDetails) {
        this.company = company;
        this.location = location;
        this.title = title;
        this.isPaid = isPaid;
        this.postedAgo = postedAgo;
        this.jobDetails = jobDetails;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public String getPostedAgo() { return postedAgo; }
    public void setPostedAgo(String postedAgo) { this.postedAgo = postedAgo; }

    public String getJobDetails() { return jobDetails; }
    public void setJobDetails(String jobDetails) { this.jobDetails = jobDetails; }
	
}