package com.placeholders.mindquest.Journals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalDTO {
    private int id;
    private String title;
    private String diaryEntry;

    public JournalDTO()
    {

    }
    public JournalDTO(int id, String title, String diaryEntry) {
        this.id = id;
        this.title = title;
        this.diaryEntry = diaryEntry;
    }
}
