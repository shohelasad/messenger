package com.visable.message.repository;

import com.visable.message.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class UserRepositoryTest {
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
    }


    @Test
    void testFindByNicknameExist() {
        Optional<User> userOptional = userRepository.findByNickname(sender.getNickname());

        assertThat(userOptional).isNotEmpty();
    }

    @Test
    void testFindByNicknameNotExist() {
        Optional<User> userOptional = userRepository.findByNickname("notExist");

        assertThat(userOptional).isNotPresent();
    }
}
