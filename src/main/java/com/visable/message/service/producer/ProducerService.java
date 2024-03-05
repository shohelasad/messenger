package com.visable.message.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visable.message.domain.Message;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.mapper.MessageMapper;
import com.visable.message.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class ProducerService {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final String messageTopic;

    public ProducerService(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate, MessageRepository messageRepository, MessageMapper messageMapper, @Value("${spring.kafka.topic.name}") String messageTopic) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.messageTopic = messageTopic;
    }

    public MessageStatus sendMessage(MessageDto event) {
        try {
            String partitioningKey = event.senderId() + "-" + event.recipientId();
            int partition = calculatePartition(partitioningKey);

            String eventAsString = objectMapper.writeValueAsString(event);
            ProducerRecord<String, String> record = new ProducerRecord<>(messageTopic, partition, partitioningKey, eventAsString);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record).toCompletableFuture();
            return future.thenApply(result -> {
                log.info("Sent message: {}", event);
                return MessageStatus.DELIVERED;
            }).exceptionally(ex -> {
                log.error("Unable to send message: {}", event, ex);
                return MessageStatus.FAILED;
            }).join();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private int calculatePartition(String partitioningKey) {
        int topicSize = kafkaTemplate.partitionsFor(messageTopic).size();
        return Math.abs(partitioningKey.hashCode()) % topicSize;
    }
}
