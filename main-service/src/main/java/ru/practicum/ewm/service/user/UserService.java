package ru.practicum.ewm.service.user;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

@Component
public interface UserService {

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
    UserDto addUser(NewUserRequest user);
    UserDto deleteUser(Long userId);
}
