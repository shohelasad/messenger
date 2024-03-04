package com.visable.message.domain.dto;

import com.visable.message.domain.enums.MessageStatus;
import java.time.LocalDateTime;

public record MessageDto(
    Long senderId,
    Long recipientId,
    String content,
    LocalDateTime timestamp,
    MessageStatus messageStatus
) {
}