package com.placeholders.mindquest.user_utils;

import com.placeholders.mindquest.role_utils.Role;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A user class for storing account data.
 * @author marius
 */
@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;//primary key that will be invisible to users

    @Column(nullable = false , unique = true, length = 90)
    private String email;//user's email

    @Column(nullable = false, length = 70)
    private String password;//password

    @Column(name = "first_name", nullable = false, length = 70)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 70)
    private String lastName;

    //many to many references to user roles (admin, guest, user...)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(name = "role_id", referencedColumnName = "id")},
            foreignKey = @ForeignKey(name = "none")
    )
    private List<Role> roles = new ArrayList<>();
    

    public User() {

    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String password , String firstName, String lastName, List<Role> roles){
        this(email, password, firstName, lastName);
        this.roles = new ArrayList<>(roles);
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email can't be null");
    }
    public void setRoles(List<Role> roles) {
        this.roles = new ArrayList<>(Objects.requireNonNull(roles, "Roles can't be null"));
    }

    public boolean isAdmin(){
        return this.getRoles().stream().anyMatch(role -> role.getName().contains("ADMIN"));
    }

    /**
     * Gets User in a form of a DTO
     * @return user's dto
     */
    public UserDTO getTransferableData(){
        return new UserDTO(id, firstName, lastName, email, password);
    }


}
