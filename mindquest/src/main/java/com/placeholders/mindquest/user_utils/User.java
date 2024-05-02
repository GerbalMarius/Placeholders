package com.placeholders.mindquest.user_utils;

import com.placeholders.mindquest.journals.Journal;
import com.placeholders.mindquest.points.Point;
import com.placeholders.mindquest.role_utils.Role;

import com.placeholders.mindquest.starting_quiz.StartingQuizInfo;
import com.placeholders.mindquest.timestamp.TimeStamp;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> points;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeStamp> timestampLog;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Journal> diaryEntries;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private StartingQuizInfo startingQuizInfo;
    

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
        this.points = new ArrayList<>();
        this.timestampLog = new ArrayList<>();
    }

    public User( String email, String password, String firstName, String lastName, List<Role> roles, List<Point> points) {
        this(email, password, firstName, lastName, roles);
        this.points = new ArrayList<>(points);
        this.timestampLog = new ArrayList<>();
    }

    public User( String email, String password, String firstName, String lastName, List<Role> roles, List<Point> points, List<Journal>diaryEntries) {
        this(email, password, firstName, lastName, roles, points);
        this.diaryEntries = new ArrayList<>(diaryEntries);
    }



    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email can't be null");
    }

    public void setRoles(List<Role> roles) {
        this.roles = new ArrayList<>(Objects.requireNonNull(roles, "Roles can't be null"));
    }

    public void addPoint(Point point){
        this.points.add(point);
    }

    public void addTimeStamp(TimeStamp timeStamp){
        this.timestampLog.add(timeStamp);
    }
    public void addJournal(Journal journal){
        this.diaryEntries.add(journal);
    }

    public void setPoints(List<Point> points) {
        this.points = new ArrayList<>(Objects.requireNonNull(points, "Points can't be null"));
    }

    public void setTimestampLog(List<TimeStamp> timestampLog) {
        this.timestampLog = new ArrayList<>(Objects.requireNonNull(timestampLog, "Timestamps log can't be null"));
    }
    public void setDiaryEntries(List<Journal> diaryEntries) {
        this.diaryEntries = new ArrayList<>(diaryEntries);
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
