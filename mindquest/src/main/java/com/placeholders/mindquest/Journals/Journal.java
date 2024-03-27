package com.placeholders.mindquest.Journals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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
    @Column(name = "diaryEntry",nullable = false, length = 400)
    private String diaryEntry;

    public Journal(){

    }
    public Journal(int id,String title, String diaryEntry) {
        this.id = id;
        this.title = title;
        this.diaryEntry = diaryEntry;
    }
}
