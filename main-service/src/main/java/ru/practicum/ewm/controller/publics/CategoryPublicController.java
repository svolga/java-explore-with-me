package ru.practicum.ewm.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.utils.Constant;
import ru.practicum.ewm.service.category.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(Constant.CATEGORIES_URL)
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(
                    name = Constant.PARAMETER_FROM,
                    defaultValue = Constant.DEFAULT_ZERO) Integer from,
            @Positive @RequestParam(
                    name = Constant.PARAMETER_SIZE,
                    defaultValue = Constant.DEFAULT_TEN) Integer size) {
        log.info("Получить список категорий с доступом public, значение from --> {}, " +
                "size --> {}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping(Constant.CATEGORY_ID_PATH_VARIABLE)
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Получить категорию с доступом public по catId --> {}", catId);
        return categoryService.getCategoryById(catId);
    }

}
