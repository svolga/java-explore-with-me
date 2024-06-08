package ru.practicum.ewm.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.compilation.CompilationService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(Constant.COMPILATIONS_URL)
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(name = Constant.PINNED_PARAMETER_NAME,
                    required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(
                    name = Constant.FROM_PARAMETER_NAME,
                    defaultValue = Constant.ZERO_DEFAULT_VALUE) Integer from,
            @Positive @RequestParam(
                    name = Constant.SIZE_PARAMETER_NAME,
                    defaultValue = Constant.TEN_DEFAULT_VALUE) Integer size) {
        log.info("Получение подборок событий, искать только закрепленные/не закрепленные подборки pinned --> {}, " +
                "from --> {}, size --> {}", pinned, from, size);
        return compilationService.getCompilations(from, size, pinned);
    }

    @GetMapping(Constant.COMPILATION_ID_PATH_VARIABLE)
    public CompilationDto getCompilationById(@PathVariable Integer compId) {
        log.info("Получение подборки событий по его compId --> {} ", compId);
        return compilationService.getCompilationById(compId);
    }
}