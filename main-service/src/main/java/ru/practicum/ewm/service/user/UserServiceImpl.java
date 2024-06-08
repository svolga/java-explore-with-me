package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.utils.errors.ErrorConstants;
import ru.practicum.ewm.utils.errors.exceptions.ConflictConstraintUniqueException;
import ru.practicum.ewm.utils.errors.exceptions.NotFoundException;
import ru.practicum.ewm.utils.logger.ListLogger;
import ru.practicum.ewm.utils.mapper.UserMapper;
import ru.practicum.ewm.utils.paging.Paging;

import java.util.List;

import static ru.practicum.ewm.utils.errors.ErrorConstants.USER_NAME_UNIQUE_VIOLATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest userData) {
            User newUser = userRepository.save(UserMapper.toUserEntity(userData));
            UserDto newUserDto = UserMapper.toUserDto(newUser);
            log.info("Создан user --> {}", newUserDto);
            return newUserDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {

        List<User> users;
        if (ids == null) {
            users = userRepository.findAll(Paging.getPageable(from, size)).getContent();
        } else {
            users = userRepository.findAllByIdIn(ids, Paging.getPageable(from, size));
        }
        ListLogger.logResultList(users);
        return UserMapper.toUserDtoList(users);
    }

    @Override
    @Transactional
    public UserDto deleteUser(Long userId) {
        UserDto deletedUser = UserMapper.toUserDto(getUserOrThrowException(userId));
        userRepository.deleteById(userId);
        log.info("Удалить user для id --> {}, user: {}", userId, deletedUser);
        return deletedUser;
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("User", userId)));
    }

}
