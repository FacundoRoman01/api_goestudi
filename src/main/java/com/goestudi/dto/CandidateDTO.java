package com.goestudi.dto;

import java.util.List;

public class CandidateDTO {
    private Long id;
    private String fullName;
    private String email;
    private String location;
    private String phone;
    private List<String> skills;

    public CandidateDTO() {}

    public CandidateDTO(Long id, String fullName, String email, String location, String phone, List<String> skills) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.location = location;
        this.phone = phone;
        this.skills = skills;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
