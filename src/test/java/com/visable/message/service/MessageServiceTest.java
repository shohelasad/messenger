package com.visable.message.service;

import com.visable.message.domain.Message;
import com.visable.message.domain.User;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.mapper.MessageMapper;
import com.visable.message.repository.MessageRepository;
import com.visable.message.service.producer.ProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ProducerService producerService;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;


    @Test
    public void testSendMessage() {
        Long userId = 1L;
        User sender = new User(1l, "sender");
        User recipient = new User(2l, "recipient");
        LocalDateTime dateTime = LocalDateTime.now();
        MessageDto messageDto = new MessageDto(1l,2l, "hello", dateTime, MessageStatus.SEND);
        Message message = new Message(1l, sender, recipient, "hello", dateTime, MessageStatus.SEND);
        when(messageMapper.messageDtoToMessage(messageDto)).thenReturn(message);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(producerService.sendMessage(any(MessageDto.class))).thenReturn(MessageStatus.DELIVERED);
        when(messageMapper.messageToMessageDto(any(Message.class))).thenReturn(messageDto);

        MessageDto result = messageService.sendMessage(userId, messageDto);
        verify(messageMapper).messageDtoToMessage(messageDto);
        verify(messageRepository).save(any(Message.class));
        verify(producerService).sendMessage(messageDto);
        verify(messageMapper).messageToMessageDto(message);
    }

    @Test
    public void testGetReceivedMessages() {
        Long recipientId = 1L;
        User recipient = new User(1l, "recipient");
        List<Message> messages = new ArrayList<>();
        when(userService.getUserById(recipientId)).thenReturn(recipient);
        when(messageRepository.findByRecipient(recipient)).thenReturn(messages);
        when(messageMapper.messageToMessageDtoList(messages)).thenReturn(new ArrayList<>());

        List<MessageDto> result = messageService.getReceivedMessages(recipientId);
        verify(userService).getUserById(recipientId);
        verify(messageRepository).findByRecipient(any(User.class));
        verify(messageMapper).messageToMessageDtoList(messages);
    }

    @Test
    public void testGetSentMessages() {
        Long senderId = 1L;
        User sender = new User(1l, "sender");
        List<Message> messages = new ArrayList<>();
        when(userService.getUserById(senderId)).thenReturn(sender);
        when(messageRepository.findBySender(sender)).thenReturn(messages);
        when(messageMapper.messageToMessageDtoList(messages)).thenReturn(new ArrayList<>());

        List<MessageDto> result = messageService.getSentMessages(senderId);
        verify(userService).getUserById(senderId);
        verify(messageRepository).findBySender(any(User.class));
        verify(messageMapper).messageToMessageDtoList(messages);
    }

    @Test
    public void testGetMessagesFromUser() {
        Long recipientId = 1L;
        Long senderId = 2L;
        User recipient = new User(1l, "recipient");
        User sender = new User(1l, "sender");
        List<Message> messages = new ArrayList<>();
        when(userService.getUserById(recipientId)).thenReturn(recipient);
        when(userService.getUserById(senderId)).thenReturn(sender);
        when(messageRepository.findByRecipientAndSender(recipient, sender)).thenReturn(messages);
        when(messageMapper.messageToMessageDtoList(messages)).thenReturn(new ArrayList<>());

        List<MessageDto> result = messageService.getMessagesFromUser(recipientId, senderId);
        verify(userService).getUserById(recipientId);
        verify(messageRepository).findByRecipientAndSender(any(User.class), any(User.class));
        verify(messageMapper).messageToMessageDtoList(messages);
    }
}
