package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.ewm.stats.mapper.HitMapper;
import ru.practicum.ewm.stats.model.Hit;
import ru.practicum.ewm.stats.repository.HitJpaRepository;
import ru.practicum.ewm.stats.utils.ListLogger;
import ru.practicum.ewm.stats.utils.validation.DateTimeValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final HitJpaRepository hitJpaRepository;

    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        Hit hit = HitMapper.toHit(endpointHit);
        Hit savedHit = hitJpaRepository.save(hit);
        log.info("Create endpoint hit --> {}", savedHit);
        return HitMapper.toEndpointHit(savedHit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        DateTimeValidator.checkStartTimeIsAfterEnd(start, end);

        List<Hit> hits = (uris == null) ?
                hitJpaRepository.findAllByTimestampBetween(start, end) :
                hitJpaRepository.findAllByUriInAndTimestampBetween(uris, start, end);

        List<ViewStats> stats = new ArrayList<>();
        if (!hits.isEmpty()) {
            if (Boolean.TRUE.equals(unique)) {
                hits = new ArrayList<>(hits.stream()
                        .collect(Collectors.toMap(Hit::getIp, Function.identity(), (hit1, hit2) -> hit1))
                        .values());

                ListLogger.logResultList(hits);
            }
            stats = mapAndSortList(hits, uris);
        }

        ListLogger.logResultList(stats);
        log.info("stats --> {}", stats);
        return stats;

    }

    private List<ViewStats> mapAndSortList(List<Hit> hits, List<String> uris) {

        Comparator<ViewStats> viewDesc = Comparator.comparing(ViewStats::getHits).reversed();

        if (uris != null) {
            Map<String, Long> uriViews = hits.stream()
                    .collect(Collectors.groupingBy((Hit::getUri), Collectors.summingLong(h -> 1)));

            return hits.stream()
                    .map(hit -> new ViewStats(hit.getApp(), hit.getUri(), uriViews.get(hit.getUri())))
                    .distinct()
                    .sorted(viewDesc)
                    .collect(Collectors.toList());
        }

        Long views = (long) hits.size();
        return hits.stream()
                .map(hit -> new ViewStats(hit.getApp(), hit.getUri(), views))
                .sorted(viewDesc)
                .collect(Collectors.toList());
    }

}