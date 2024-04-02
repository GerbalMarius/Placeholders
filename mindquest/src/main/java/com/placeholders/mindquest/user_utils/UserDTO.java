package com.placeholders.mindquest.user_utils;

import com.placeholders.mindquest.Settingsmodels.ProfilePhoto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * User data transfer object used to transfer data between controller layer and frontend layer, basically the visible part of user data.
 * @author marius
 */
@Data
public final class UserDTO {
    private long id;

    private String firstName;
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*([A-Za-z]{2,})$")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private ProfilePhoto pfp;

    public UserDTO(){
        this(0, "", "", "");
    }

    public UserDTO(long id, String firstName, String lastName, String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(long id, String firstName, String lastName,String email, String password){
        this(id, firstName, lastName, email);
        this.password = password;
    }

}
