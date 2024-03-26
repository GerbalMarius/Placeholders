package com.placeholders.mindquest.user_utils;

import com.placeholders.mindquest.Settingsmodels.ProfilePhoto;
import com.placeholders.mindquest.role_utils.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A user class for storing account data.
 * @author marius
 */
@Entity
@Getter
@Setter
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
            inverseJoinColumns ={@JoinColumn(name = "role_id", referencedColumnName = "id")}
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

    /**
     * Use for logging if needed
     * @return formatted user object
     */
    @Override
    public String toString() {
        return String.format("User(id:%d, email:%s, firstName:%s, lastName:%s, roles:%s)", getId(), getEmail(),
                getFirstName(), getLastName(), getRoles());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof User other)) return false;

        return id == other.id && email.equals(other.email) && firstName.equals(other.firstName)
                && lastName.equals(other.lastName) && Arrays.equals(roles.toArray(), other.roles.toArray());
    }

    @Override
    public int hashCode() {
       return Objects.hash(id, email, firstName, lastName, roles.hashCode());
    }
}
