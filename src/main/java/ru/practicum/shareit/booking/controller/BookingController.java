package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.conroller.UserController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public BookingResponseDto getById(@RequestHeader(UserController.headerUserId) Long userId,
                                      @PathVariable Long id) {
        log.info("Получен запрос GET /bookings/id  запрос на вещь с id" + id);
        return bookingService.getById(userId, id);
    }

    @GetMapping
    public List<BookingResponseDto> getAllByBookerId(@RequestHeader(UserController.headerUserId) Long userId,
                                                     @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Получен запрос всех вещей бронирующего GET /bookings " + userId);
        return bookingService.getAllByBookerId(userId, bookingService.checkStateValid(state));
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllByOwnerId(@RequestHeader(UserController.headerUserId) Long userId,
                                                    @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Получен запрос всех вещей владельца GET /bookings/owner " + userId);
        return bookingService.getAllByOwnerId(userId, bookingService.checkStateValid(state));
    }

    @PostMapping
    public BookingResponseDto add(@RequestHeader(UserController.headerUserId) Long userId,
                                  @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Получен запрос POST /bookings " + userId);
        return bookingService.add(userId, bookingRequestDto);
    }

    @PatchMapping("/{id}")
    public BookingResponseDto update(@RequestHeader(UserController.headerUserId) Long userId,
                                     @PathVariable Long id,
                                     @RequestParam() Boolean approved) {
        log.info("Получен запрос PATCH /bookings/id " + " ! статус брони вещи с id" + id + ": забронировано=" + approved + " юзер с id" + userId);
        return bookingService.update(userId, id, approved);
    }
}