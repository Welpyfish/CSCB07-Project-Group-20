package com.group20.cscb07project.EmergencyInfo.Medications;

public class MedicationItem {
    private String id;
    private String name;
    private String dosage;

    public MedicationItem() {
    }

    public MedicationItem(String id, String name, String dosage, String phone) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}