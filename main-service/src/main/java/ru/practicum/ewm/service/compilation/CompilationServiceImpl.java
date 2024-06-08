package ru.practicum.ewm.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.utils.errors.ErrorConstants;
import ru.practicum.ewm.utils.errors.exceptions.ConflictConstraintUniqueException;
import ru.practicum.ewm.utils.errors.exceptions.NotFoundException;
import ru.practicum.ewm.utils.logger.ListLogger;
import ru.practicum.ewm.utils.mapper.CompilationMapper;
import ru.practicum.ewm.utils.paging.Paging;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.utils.errors.ErrorConstants.COMPILATION_TITLE_UNIQUE_VIOLATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilation) {

        List<Event> events = compilation.getEvents() == null ? new ArrayList<>() : getEvents(compilation.getEvents());
        try {
            Compilation newCompilation = compilationRepository
                    .save(CompilationMapper.toCompilationEntity(compilation, events));
            log.info("New compilation: {} added", newCompilation);
            return CompilationMapper.toCompilationDto(newCompilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictConstraintUniqueException(COMPILATION_TITLE_UNIQUE_VIOLATION);
        }
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest request) {

        Compilation compilation = compilationRepository.getReferenceById(compId);
        List<Event> events;

        if (request.getPinned() != null) {
            compilation = compilation.toBuilder().pinned(request.getPinned()).build();
        }
        if (request.getTitle() != null) {
            compilation = compilation.toBuilder().title(request.getTitle()).build();
        }
        if (request.getEvents() != null && !request.getEvents().isEmpty()) {
            events = getEvents(request.getEvents());
            compilation = compilation.toBuilder().events(events).build();
        }
        Compilation updatedCompilation = compilationRepository.save(compilation);
        log.info("Compilation: {} updated", updatedCompilation);

        return CompilationMapper.toCompilationDto(updatedCompilation);
    }

    @Override
    @Transactional
    public CompilationDto deleteCompilation(Integer compId) {
        CompilationDto deleted = CompilationMapper.toCompilationDto(getCompilationOrThrowException(compId));
        compilationRepository.deleteById(compId);
        log.info("Delete compilation with id: {}, compilation: {}", compId, deleted);
        return deleted;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(Paging.getPageable(from, size)).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned);
        }
        ListLogger.logResultList(compilations);
        return CompilationMapper.toCompilationDtoList(compilations);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Integer compId) {
        Compilation compilation = getCompilationOrThrowException(compId);
        log.info("Compilation {} was found by id {}", compilation, compId);
        return CompilationMapper.toCompilationDto(compilation);
    }

    private List<Event> getEvents(List<Long> ids) {
        return eventRepository.findAllByIdIn(ids);
    }

    private Compilation getCompilationOrThrowException(Integer compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorConstants.getNotFoundMessage("Compilation", compilationId)));
    }

}
