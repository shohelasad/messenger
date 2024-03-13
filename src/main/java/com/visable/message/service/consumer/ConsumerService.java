package com.visable.message.service.consumer;

import com.visable.message.domain.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void receiveMessage(ConsumerRecord<String, MessageDto> eventRecord) {
        log.info("Message consumed {}", eventRecord);
        MessageDto event = eventRecord.value();
        String destination = "/topic/user/" + event.recipientId();
        messagingTemplate.convertAndSend(destination, event.content());
        log.info("Message send to: {}, with content: {}", event.recipientId(), event.content());
    }
}
