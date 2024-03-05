package com.visable.message.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visable.message.domain.Message;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.mapper.MessageMapper;
import com.visable.message.repository.MessageRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
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

    public void sendMessage(MessageDto event) {
        try {
            String partitioningKey = event.senderId() + "-" + event.recipientId();
            int partition = calculatePartition(partitioningKey);

            String message = objectMapper.writeValueAsString(event);
            ProducerRecord<String, String> record = new ProducerRecord<>(messageTopic, partition, partitioningKey, message);

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record).toCompletableFuture();
            future.whenComplete((result, ex) -> {
                Message sendMessage = messageMapper.messageDtoToMessage(event);
                if (ex == null) {
                    log.info("Sent message: {}", event);
                    sendMessage.setMessageStatus(MessageStatus.DELIVERED);
                } else {
                    log.error("Unable to send message: {}", event, ex);
                    sendMessage.setMessageStatus(MessageStatus.FAILED);
                }
                messageRepository.save(sendMessage);
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private int calculatePartition(String partitioningKey) {
        int topicSize = kafkaTemplate.partitionsFor(messageTopic).size();
        return Math.abs(partitioningKey.hashCode()) % topicSize;
    }
}
