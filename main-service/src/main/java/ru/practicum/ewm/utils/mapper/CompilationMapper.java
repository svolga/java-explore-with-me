package ru.practicum.ewm.utils.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.entity.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilationEntity(NewCompilationDto compilation, List<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(compilation.getPinned() != null && compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getCompilationId())
                .events(EventMapper.toEventShortDtoList(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
