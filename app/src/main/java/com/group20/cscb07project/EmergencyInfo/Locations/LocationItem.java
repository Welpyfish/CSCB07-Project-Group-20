package com.group20.cscb07project.EmergencyInfo.Locations;

public class LocationItem {
    private String id;
    private String address;
    private String note;

    public LocationItem() {
    }

    public LocationItem(String id, String address, String note, String phone) {
        this.id = id;
        this.address = address;
        this.note = note;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}