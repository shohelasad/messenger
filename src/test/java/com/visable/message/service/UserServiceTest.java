package com.visable.message.service;

import com.visable.message.domain.User;
import com.visable.message.domain.dto.UserDto;
import com.visable.message.mapper.UserMapper;
import com.visable.message.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser_Success() {
        UserDto userDto = new UserDto(1l, "testuser");
        User user = new User(1l, "testuser");
        when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);
        UserDto result = userService.createUser(userDto);
        assertNotNull(result);
    }

    @Test
    public void testCreateUser_NicknameAlreadyExists() {
        UserDto userDto = new UserDto(1l, "existingNickname");
        String existingNickname = "existingNickname";
        when(userRepository.findByNickname(existingNickname)).thenReturn(Optional.of(new User()));
        assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
    }

    @Test
    public void testGetUserById_UserExists() {
        Long userId = 1L;
        User user = new User(1l, "testuser");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        User result = userService.getUserById(userId);
        assertNotNull(result);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
    }
}
