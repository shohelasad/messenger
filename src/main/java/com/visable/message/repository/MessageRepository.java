package com.visable.message.repository;

import com.visable.message.domain.Message;
import com.visable.message.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySender(User sender);
    List<Message> findByRecipient(User recipient);
    List<Message> findByRecipientAndSender(User recipient, User sender);
}
