package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.category.CategoryService;

import org.springframework.http.HttpStatus;
import ru.practicum.ewm.utils.Constant;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(Constant.ADMIN_URL + Constant.CATEGORIES_URL)
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("Добавить category: {}", categoryDto);
        return categoryService.addCategory(categoryDto);
    }

    @DeleteMapping(Constant.CATEGORY_ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CategoryDto deleteCategory(@PathVariable Long catId) {
        log.info("Удалить категорию categoryId --> {}", catId);
        return categoryService.deleteCategory(catId);
    }

    @PatchMapping(Constant.CATEGORY_ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Изменить категорию categoryId --> {}, категория --> {}", catId, categoryDto);
        return categoryService.updateCategory(catId, categoryDto);
    }
}
