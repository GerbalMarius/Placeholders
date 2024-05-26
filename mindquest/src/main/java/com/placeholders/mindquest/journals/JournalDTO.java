package com.placeholders.mindquest.journals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalDTO {
    private int id;
    private String title;
    private String diaryEntry;
    private LocalDateTime date;


    public String getLocalDateFormat(){
        return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT));
    }
}
