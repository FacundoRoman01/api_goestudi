package com.goestudi.dto;

public class JobDTO {
	
	  private String company;
	    private String location;
	    private String title;
	    private boolean isPaid;
	    private String postedAgo;
	    private String jobDetails;

	    // Constructor vacío
	    public JobDTO() {}

	    
	    public JobDTO(String company, String location, String title, boolean isPaid, String postedAgo, String jobDetails) {
	        this.company = company;
	        this.location = location;
	        this.title = title;
	        this.isPaid = isPaid;
	        this.postedAgo = postedAgo;
	        this.jobDetails = jobDetails;
	    }

	    // Getters y Setters
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
