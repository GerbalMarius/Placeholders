package com.placeholders.mindquest.timestamp;



import com.placeholders.mindquest.user_utils.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Table(name = "quiz_history")
public class TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "completion_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "tip_text")
    private String tipContent;

    @Column(name = "score")
    private int points;

    public TimeStamp(LocalDateTime timestamp, String tipContent, int points) {
        this.timestamp = timestamp;
        this.tipContent = tipContent;
        this.points = points;

    }

    public TimeStamp() {

    }

    /**
     * For displaying on the app (seconds are redundant.)
     * @return date format in year, month, day, hours and minutes
     */
    public String getFormatWithMinutesAndHours(){
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String scorePercentageFormat(){
        return points + "%";
    }
}
