package com.placeholders.mindquest.journals;

import com.placeholders.mindquest.user_utils.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@Table(name ="journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, length = 70)
    private String title;

    @Column(name = "diaryEntry",nullable = false, length = 800)
    private String diaryEntry;

    @Column(name ="entryDate",nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Journal(){

    }
    public Journal(int id,String title, String diaryEntry) {
        this.id = id;
        this.title = title;
        this.diaryEntry = diaryEntry;
    }
}
