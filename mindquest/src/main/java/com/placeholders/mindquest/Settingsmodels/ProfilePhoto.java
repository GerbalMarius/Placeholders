package com.placeholders.mindquest.Settingsmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProfilePhoto {
    private String id;
    private String fileName;
    @JsonIgnore
    private byte[] data;

    public ProfilePhoto() {
    }
    public ProfilePhoto(String id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
