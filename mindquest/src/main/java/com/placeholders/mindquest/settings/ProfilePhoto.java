package com.placeholders.mindquest.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity
@Setter
@Getter
@Table(name = "profilePhoto")
public class ProfilePhoto {
    @Id
    private long id;

    @JsonIgnore
    @Column(name = "data", nullable = false, length = 1024*1024*5)
    private byte[] data;

    public ProfilePhoto() {
    }
    public ProfilePhoto(long id) {
        this.id = id;
    }


}
