package ru.practicum.ewm.utils.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.ewm.enums.SortType;

public class Paging {

    public static Pageable getPageable(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

    public static Pageable getPageable(Integer from, Integer size, SortType sortType) {

        int page = from / size;
        if (sortType == null) {
            return PageRequest.of(page, size);
        } else {
            String fieldName = sortType.name().toLowerCase();
            if ("created_on".equals(fieldName)) {
                fieldName = "createdOn";
            }
            if ("event_date".equals(fieldName)) {
                fieldName = "eventDate";
            } else if ("views".equals(fieldName)) {
                fieldName = "views";
            }
            Sort sort = Sort.by(Sort.Direction.DESC, fieldName);
            return PageRequest.of(page, size, sort);
        }
    }

}
