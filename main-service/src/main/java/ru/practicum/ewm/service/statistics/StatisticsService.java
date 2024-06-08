package ru.practicum.ewm.service.statistics;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public interface StatisticsService {
    void saveStats(HttpServletRequest request);

    Map<Long, Long> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
