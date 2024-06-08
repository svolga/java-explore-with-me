package ru.practicum.ewm.utils.errors.exceptions;

public class ConflictConstraintUniqueException extends RuntimeException {
    public ConflictConstraintUniqueException(String message) {
        super(message);
    }
}
