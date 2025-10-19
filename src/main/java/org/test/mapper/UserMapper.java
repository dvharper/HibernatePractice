package org.test.mapper;

import org.test.dto.UserDto;
import org.test.model.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public static User toEntity(UserDto dto) {
        User user = new User(dto.getName(), dto.getEmail(), dto.getAge());
        user.setId(dto.getId());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
    }
}
