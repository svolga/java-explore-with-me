package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    List<Compilation> findAllByPinned(boolean pinned);
}
