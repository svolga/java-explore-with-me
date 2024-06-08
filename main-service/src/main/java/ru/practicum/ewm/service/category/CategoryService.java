package ru.practicum.ewm.service.category;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

import java.util.List;

@Component
public interface CategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId);

    CategoryDto addCategory(NewCategoryDto category);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, CategoryDto category);
}
