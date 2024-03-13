package com.visable.message.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessages {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}
