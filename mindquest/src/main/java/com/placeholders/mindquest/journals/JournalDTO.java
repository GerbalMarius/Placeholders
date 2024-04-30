package com.placeholders.mindquest.journals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalDTO {
    private int id;
    private String title;
    private String diaryEntry;

}
