package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.user.UserService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.ADMIN_URL + Constant.USERS_URL)
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(name = Constant.IDS_PARAMETER_NAME,
                    required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(
                    name = Constant.FROM_PARAMETER_NAME,
                    defaultValue = Constant.ZERO_DEFAULT_VALUE) Integer from,
            @Positive @RequestParam(
                    name = Constant.SIZE_PARAMETER_NAME,
                    defaultValue = Constant.TEN_DEFAULT_VALUE) Integer size) {
        log.info("Получить для admin пользователей по ids --> {}, from --> {}, size --> {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public UserDto addUser(@Valid @RequestBody NewUserRequest user) {
        log.info("Добавить админом пользователя user --> {}", user);
        return userService.addUser(user);
    }

    @DeleteMapping(Constant.USER_ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto deleteUser(@PathVariable Long userId) {
        log.info("Удалить админом пользователя по userId --> {}", userId);
        return userService.deleteUser(userId);
    }
}