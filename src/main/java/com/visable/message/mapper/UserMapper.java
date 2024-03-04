package com.visable.message.mapper;

import com.visable.message.domain.User;
import com.visable.message.domain.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getNickname());
    }

    public User userDtoToUser(UserDto userDto) {
        return new User(userDto.userId(), userDto.nickName());
    }
}
