package ru.practicum.shareit.exception;

public class EmailIsNotUniqueException extends RuntimeException {
    public EmailIsNotUniqueException(String message) {
        super(message);
    }
}