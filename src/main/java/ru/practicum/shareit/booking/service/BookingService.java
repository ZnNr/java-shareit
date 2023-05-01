package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;

import java.util.List;

public interface BookingService {
    BookingResponseDto getById(Long userId, Long id);

    List<BookingResponseDto> getAllByBookerId(Long userId, BookingState bookingState);

    List<BookingResponseDto> getAllByOwnerId(Long userId, BookingState bookingState);

    BookingResponseDto add(Long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto update(Long userId, Long id, Boolean approved);

    BookingState checkStateValid(String state);
}