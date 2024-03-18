package com.placeholders.mindquest.user_utils;

import com.placeholders.mindquest.Settingsmodels.ProfilePhoto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * User data transfer object used to transfer data between controller layer and frontend layer, basically the visible part of user data.
 * @author marius
 */
public class UserDTO {
    private long id;

    private String firstName;
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private ProfilePhoto pfp;

    public UserDTO(){

    }

    public UserDTO(long id, String firstName, String lastName, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(long id, String firstName, String lastName, String email, ProfilePhoto pfp){
        this(id, firstName, lastName, email);
        this.pfp = pfp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProfilePhoto getPfp() {
        return pfp;
    }

    public void setPfp(ProfilePhoto pfp) {
        this.pfp = pfp;
    }
}
