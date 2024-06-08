package ru.practicum.ewm.utils.errors;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorConstants {
    public static final String UNKNOWN_SORT_TYPE = "Неизвестное значение SortType :";
    public static final String UNKNOWN_EVENT_STATE = "Неизвестное значение EventState :";
    public static final String UNKNOWN_ACTION = "Неизвестное значение StateAction :";

    public static final String CATEGORY_IS_NOT_EMPTY = "Категория непустая";
    public static final String EVENT_IS_PUBLISHED = "Нельзя опубликовать event, неверный state: PUBLISHED";

    public static final String EVENT_IS_NOT_PUBLISHED_YET = "Нет доступа";
    public static final String START_AFTER_END = "Дата начала должна быть раньше даты окончания";

    public static final String TIME_IS_LESS_THAN_ONE_HOUR_BEFORE_START = "Нарушение значения времени:"
            + " значение меньше одного часа до старта события";
    public static final String TIME_IS_LESS_THAN_TWO_HOUR_BEFORE_START = "Нарушение значения времени:"
            + " значение меньше двух часов до старта события";
    public static final String ONLY_FOR_INITIATOR = "Доступ только для инициатора собятия";

    public static final String NOT_FOR_INITIATOR = "Нет доступа для инициатора собятия";

    public static final String LIMIT = "Достигнут лимит на участие";
    public static final String ONLY_FOR_REQUESTER = "Доступ только для requester";

    public static final String REPEATED_REQUEST = "Доступен только один запрос от пользователя";
    public static final String NOT_PENDING = "Список запросов на обновление содержит не ожидающие обработки запросы.";


    public static final String INCORRECTLY_MADE_REQUEST = "Неверный запрос";
    public static final String DATA_INTEGRITY_VIOLATION = "Нарушена целостность данных";
    public static final String OBJECT_NOT_FOUND = "Запрошенный обьект не найден";
    public static final String ACTION_IS_NOT_ALLOWED = "Для запрошенной операции условия не выполнены";

    public static final String CATEGORY_NAME_UNIQUE_VIOLATION = "could not execute statement; "
            + "SQL [n/a]; constraint [uq_category_name]; "
            + "nested exception is org.hibernate.exception.ConstraintViolationException: "
            + "could not execute statement";
    public static final String USER_NAME_UNIQUE_VIOLATION = "could not execute statement; "
            + "SQL [n/a]; constraint [uq_user_name]; "
            + "nested exception is org.hibernate.exception.ConstraintViolationException: "
            + "could not execute statement";
    public static final String COMPILATION_TITLE_UNIQUE_VIOLATION = "could not execute statement; "
            + "SQL [n/a]; constraint [uq_compilation_title]; "
            + "nested exception is org.hibernate.exception.ConstraintViolationException: "
            + "could not execute statement";

    public static String getNotFoundMessage(String name, Number id) {
        return String.format("Не найдено name = %s для id = %d", name, id);
    }
}