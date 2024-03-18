package com.placeholders.mindquest.Settingsmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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


}
