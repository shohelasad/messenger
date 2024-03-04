package com.visable.message.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsumerService {

    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.consumer.group.id}")
    public void receiveMessage(ConsumerRecord<String, MessageDto> record) {
        log.info("Message consumed {}", record);
        MessageDto event = record.value();
        String destination = "/topic/user/" + event.recipientId();
        messagingTemplate.convertAndSend(destination, event.content());
        log.info("Message send to: {}, with content: {}", event.recipientId(), event.content());
    }
}
