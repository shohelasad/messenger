package com.visable.message.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(MessageController.class)
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MessageService messageService;

    @Test
    void testSendMessage() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        MessageDto messageDto = new MessageDto(1l,2l, "hello", dateTime, MessageStatus.SEND);
        String messageDtoJson = objectMapper.writeValueAsString(messageDto);
        Mockito.when(messageService.sendMessage(ArgumentMatchers.anyLong(), ArgumentMatchers.any(MessageDto.class)))
                .thenReturn(messageDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", "1")
                        .content(messageDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetSentMessages() throws Exception {
        List<MessageDto> sentMessages = new ArrayList<>();
        Mockito.when(messageService.getSentMessages(ArgumentMatchers.anyLong()))
                .thenReturn(sentMessages);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/messages/sent")
                        .header("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetReceivedMessages() throws Exception {
        List<MessageDto> receivedMessages = new ArrayList<>();
        Mockito.when(messageService.getReceivedMessages(ArgumentMatchers.anyLong()))
                .thenReturn(receivedMessages);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/messages/received")
                        .header("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetMessagesFromUser() throws Exception {
        List<MessageDto> messagesFromUser = new ArrayList<>();
        Mockito.when(messageService.getMessagesFromUser(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenReturn(messagesFromUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/messages/sender/{senderId}", "2")
                        .header("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
