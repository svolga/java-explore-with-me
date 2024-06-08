package ru.practicum.ewm.service.compilation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;

import java.util.List;

@Component
public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto compilation);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest compilation);

    CompilationDto deleteCompilation(Integer compId);

    List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned);

    CompilationDto getCompilationById(Integer compId);
}
