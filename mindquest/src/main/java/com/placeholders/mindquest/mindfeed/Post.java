package com.placeholders.mindquest.mindfeed;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "source", nullable = false)
    private String source;

    public Post() {
    }

    public Post(String thumbnail, String description, String source) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.source = source;
    }
}