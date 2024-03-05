package com.visable.message.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visable.message.domain.Message;
import com.visable.message.domain.User;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.mapper.MessageMapper;
import com.visable.message.repository.MessageRepository;
import com.visable.message.service.UserService;
import com.visable.message.service.producer.ProducerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class ProducerServiceTest {
    private ProducerService producerService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    private String messageTopic = "message-topic";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        producerService = new ProducerService(objectMapper, kafkaTemplate, messageRepository, messageMapper, messageTopic);
    }

    @Test
    public void testSendMessage_Success() throws JsonProcessingException {
        User sender = new User(1l, "sender");
        User recipient = new User(2l, "recipient");
        LocalDateTime dateTime = LocalDateTime.now();
        MessageDto messageDto = new MessageDto(1l,2l, "hello", dateTime, MessageStatus.SEND);
        String messageDtoJson = objectMapper.writeValueAsString(messageDto);

        when(objectMapper.writeValueAsString(messageDto)).thenReturn(messageDtoJson);
        List<PartitionInfo> partitionInfoList = new ArrayList<>();
        partitionInfoList.add(new PartitionInfo(messageTopic, 0, null, null, null));
        when(kafkaTemplate.partitionsFor(anyString())).thenReturn(partitionInfoList);
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.complete(mock(SendResult.class));
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        producerService.sendMessage(messageDto);
        verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
    }
}
