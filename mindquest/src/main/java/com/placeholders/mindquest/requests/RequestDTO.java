package com.placeholders.mindquest.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class RequestDTO {

    private long id;
    private String message;
    private String email;
}