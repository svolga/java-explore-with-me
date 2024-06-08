package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.service.compilation.CompilationService;
import ru.practicum.ewm.utils.Constant;

import javax.validation.Valid;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(Constant.ADMIN_URL + Constant.COMPILATIONS_URL)
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        log.info("Добавление новой подборки --> {}", compilation);
        return compilationService.addCompilation(compilation);
    }

    @PatchMapping(Constant.COMPILATION_ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Integer compId,
                                            @Valid @RequestBody UpdateCompilationRequest compilation) {
        log.info("Изменить подборку по id --> {} , данные -->  {}", compId, compilation);
        return compilationService.updateCompilation(compId, compilation);
    }

    @DeleteMapping(Constant.COMPILATION_ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompilationDto deleteCompilation(@PathVariable Integer compId) {
        log.info("Удалить подборку по id --> {}", compId);
        return compilationService.deleteCompilation(compId);
    }
}