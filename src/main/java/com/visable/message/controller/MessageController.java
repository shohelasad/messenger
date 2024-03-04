package com.visable.message.controller;

import com.visable.message.domain.dto.MessageDto;
import com.visable.message.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestHeader("userId") Long userId, @RequestBody @Valid MessageDto messageDto) {
        log.info("Message send with userId: {} and content:{}", userId, messageDto);
        return ResponseEntity.ok(messageService.sendMessage(userId, messageDto));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<MessageDto>> getSentMessages(@RequestHeader("userId") Long userId) {
        log.info("Get sent messages with sender id: {}", userId);
        List<MessageDto> messages = messageService.getSentMessages(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/received")
    public ResponseEntity<List<MessageDto>> getReceivedMessages(@RequestHeader("userId") Long userId) {
        log.info("Get messages with userId: {}", userId);
        List<MessageDto> messages = messageService.getReceivedMessages(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<MessageDto>> getMessagesFromUser(@RequestHeader("userId")  Long userId, @PathVariable Long senderId) {
        log.info("Get messages for user id:{}, with sender id", userId, senderId);
        List<MessageDto> messages = messageService.getMessagesFromUser(userId, senderId);
        return ResponseEntity.ok(messages);
    }
}