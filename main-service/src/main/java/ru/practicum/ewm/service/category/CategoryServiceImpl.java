package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.utils.errors.ErrorConstants;
import ru.practicum.ewm.utils.errors.exceptions.ConflictConstraintUniqueException;
import ru.practicum.ewm.utils.errors.exceptions.NotAllowedException;
import ru.practicum.ewm.utils.errors.exceptions.NotFoundException;
import ru.practicum.ewm.utils.logger.ListLogger;
import ru.practicum.ewm.utils.mapper.CategoryMapper;
import ru.practicum.ewm.utils.paging.Paging;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<Category> categories = categoryRepository.findAll(Paging.getPageable(from, size)).getContent();
        ListLogger.logResultList(categories);
        return CategoryMapper.toCategoryDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.getNotFoundMessage("Category", catId)));
        log.info("Category --> {} was found by id --> {}", category, catId);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto categoryData) {
        try {
            Category newCategory = categoryRepository.save(CategoryMapper.toCategoryEntity(categoryData));
            CategoryDto newCategoryDto = CategoryMapper.toCategoryDto(newCategory);
            log.info("Добавлена Category --> {}", newCategoryDto);
            return newCategoryDto;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictConstraintUniqueException(ErrorConstants.CATEGORY_NAME_UNIQUE_VIOLATION);
        }
    }

    @Override
    @Transactional
    public CategoryDto deleteCategory(Long catId) {
        CategoryDto deletedCategory = CategoryMapper.toCategoryDto(getCategoryOrThrowException(catId));
        checkIfEventsExistByCategory(catId);
        categoryRepository.deleteById(catId);
        log.info("Удалена category для id --> {}, category --> {}", catId, deletedCategory);
        return deletedCategory;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto dto) {
        checkIfCategoryNameIsUnique(catId, dto.getName());
        Category category = getCategoryOrThrowException(catId);
        Category updated = category.toBuilder()
                .name(dto.getName())
                .build();
        Category updatedCategory = categoryRepository.save(updated);
        CategoryDto updatedCategoryDto = CategoryMapper.toCategoryDto(updatedCategory);
        log.info("Обновлена Category --> {}", updatedCategoryDto);
        return updatedCategoryDto;
    }

    private Category getCategoryOrThrowException(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorConstants.getNotFoundMessage("Category", categoryId)));
    }

    private void checkIfEventsExistByCategory(Long catId) {
        if (eventRepository.existsByCategory_Id(catId)) {
            throw new NotAllowedException(ErrorConstants.CATEGORY_IS_NOT_EMPTY);
        }
    }

    private void checkIfCategoryNameIsUnique(Long catId, String name) {
        if (categoryRepository.existsByNameAndIdNot(name, catId)) {
            log.info("!!!! Найдено занятое имя категории --> {}", name);
            throw new ConflictConstraintUniqueException(ErrorConstants.CATEGORY_NAME_UNIQUE_VIOLATION);
        }
    }
}