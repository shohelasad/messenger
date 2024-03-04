package com.visable.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.service.consumer.ConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class ConsumerServiceTest {

    @InjectMocks
    private ConsumerService consumerService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReceiveMessage_Success() {
        String content = "Test message";
        Long recipientId = 2L;
        LocalDateTime dateTime = LocalDateTime.now();
        MessageDto messageDto = new MessageDto(1l,2l, content, dateTime, MessageStatus.SEND);

        ConsumerRecord<String, MessageDto> record = new ConsumerRecord<>("topic", 0, 0L, "key", messageDto);
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(MessageDto.class));
        consumerService.receiveMessage(record);
        verify(messagingTemplate).convertAndSend("/topic/user/" + recipientId, content);
    }
}
