package com.visable.message.mapper;

import com.visable.message.domain.Message;
import com.visable.message.domain.User;
import com.visable.message.domain.dto.MessageDto;
import com.visable.message.domain.enums.MessageStatus;
import com.visable.message.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserService userService;

    public MessageDto messageToMessageDto(Message message) {
        return new MessageDto(message.getSender().getId(), message.getRecipient().getId(), message.getContent(), message.getTimestamp(), message.getMessageStatus());
    }

    public List<MessageDto> messageToMessageDtoList(List<Message> messages) {
        return messages.stream().map(message -> messageToMessageDto(message)).toList();
    }

    public Message messageDtoToMessage(MessageDto messageDto) {
        User recipient = userService.getUserById(messageDto.recipientId());
        User sender = userService.getUserById(messageDto.senderId());
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(messageDto.content());
        message.setTimestamp(messageDto.timestamp());
        message.setMessageStatus(MessageStatus.SEND);
        return message;
    }
}
