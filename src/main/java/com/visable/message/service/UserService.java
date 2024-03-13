package com.visable.message.service;

import com.visable.message.domain.User;
import com.visable.message.domain.dto.UserDto;
import com.visable.message.mapper.UserMapper;
import com.visable.message.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        String nickname = userDto.nickName();
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new DuplicateKeyException("Nickname already exists");
        }

        User user = userMapper.userDtoToUser(userDto);
        return userMapper.userToUserDto(userRepository.save(user));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}