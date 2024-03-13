package com.visable.message.service;

import com.visable.message.domain.Message;
import com.visable.message.domain.User;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.mapper.MessageMapper;
import com.visable.message.repository.MessageRepository;
import com.visable.message.service.producer.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final ProducerService producerService;
    private final MessageMapper messageMapper;

    public MessageDto sendMessage(Long userId, MessageDto messageDto)  {
        if (messageDto.recipientId().equals(userId)) throw new IllegalArgumentException("Cannot send message to yourself");

        MessageDto messageEvent = new MessageDto(userId, messageDto.recipientId(), //set userId
                messageDto.content(), messageDto.timestamp(), messageDto.messageStatus());
        Message message = messageMapper.messageDtoToMessage(messageEvent);
        MessageStatus status = producerService.sendMessage(messageEvent);
        message.setMessageStatus(status);
        message = messageRepository.save(message);
        return messageMapper.messageToMessageDto(message);
    }

    public List<MessageDto> getReceivedMessages(Long recipientId) {
        User recipient = userService.getUserById(recipientId);
        List<Message> messages = messageRepository.findByRecipient(recipient);
        return messageMapper.messageToMessageDtoList(messages);
    }

    public List<MessageDto> getSentMessages(Long senderId) {
        User sender = userService.getUserById(senderId);
        List<Message> messages = messageRepository.findBySender(sender);
        return messageMapper.messageToMessageDtoList(messages);
    }

    public List<MessageDto> getMessagesFromUser(Long recipientId, Long senderId) {
        User recipient = userService.getUserById(recipientId);
        User sender = userService.getUserById(senderId);
        List<Message> messages = messageRepository.findByRecipientAndSender(recipient, sender);
        return messageMapper.messageToMessageDtoList(messages);
    }
}