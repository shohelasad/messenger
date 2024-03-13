package com.visable.message.repository;

import com.visable.message.domain.Message;
import com.visable.message.domain.User;
import com.visable.message.domain.enums.MessageStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User recipient;

    @BeforeAll
    void setUp() {
        sender = new User(1l, "sender");
        userRepository.save(sender);
        recipient = new User(2l, "recipient");
        userRepository.save(recipient);
        LocalDateTime dateTime = LocalDateTime.now();
        Message message1 = new Message(1l, sender, recipient, "hello1", dateTime, MessageStatus.SEND);
        Message message2 = new Message(2l, sender, recipient, "hello2", dateTime, MessageStatus.SEND);
        List<Message> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        messageRepository.saveAll(messages);
    }


    @Test
    void testFindBySender() {
        List<Message> result = messageRepository.findBySender(sender);

        assertThat(result).isNotEmpty().hasSize(2);
    }

    @Test
    void testFindByRecipient() {
        List<Message> result = messageRepository.findByRecipient(recipient);

        assertThat(result).isNotEmpty().hasSize(2);
    }

    @Test
    void testFindByRecipientAndSender() {
        List<Message> result = messageRepository.findByRecipientAndSender(recipient, sender);

        assertThat(result).isNotEmpty().hasSize(2);
    }
}
