package ru.practicum.ewm.controller.publics;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.service.comment.CommentService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(Constant.COMMENTS_URL)
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments(
            @RequestParam(
                    name = Constant.PARAMETER_SORT,
                    required = false) String sort,
            @PositiveOrZero @RequestParam(
            name = Constant.PARAMETER_FROM,
            defaultValue = Constant.DEFAULT_ZERO) Integer from,
                                 @Positive @RequestParam(
                                         name = Constant.PARAMETER_SIZE,
                                         defaultValue = Constant.DEFAULT_TEN) Integer size) {
        log.info("Получить все комментарии с доступом public from --> {}, size --> {}", from, size);
        return commentService.getComments(sort, from, size);
    }

}
