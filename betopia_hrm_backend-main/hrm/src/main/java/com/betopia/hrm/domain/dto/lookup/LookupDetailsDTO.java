package com.betopia.hrm.domain.dto.lookup;


public class LookupDetailsDTO {
    private Long id;
    private String name;        // From LookupSetupDetails.name
    private String details;     // From LookupSetupDetails.details
    private String setupName;   // From LookupSetupDetails.setup.name (This is your 'lookupType')

    public LookupDetailsDTO() {}

    public LookupDetailsDTO(Long id, String name, String details, String setupName) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.setupName = setupName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSetupName() { // New getter for the setup name/lookup type
        return setupName;
    }

    public void setSetupName(String setupName) { // New setter
        this.setupName = setupName;
    }
}