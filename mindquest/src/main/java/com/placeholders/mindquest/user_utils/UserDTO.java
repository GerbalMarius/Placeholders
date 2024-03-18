package com.placeholders.mindquest.user_utils;

import com.placeholders.mindquest.Settingsmodels.ProfilePhoto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * User data transfer object used to transfer data between controller layer and frontend layer, basically the visible part of user data.
 * @author marius
 */
@Getter
@Setter
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
}
