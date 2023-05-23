package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.markers.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader(Constants.headerUserId) Long userId,
                                          @PathVariable Long id) {
        log.info("Получен запрос GET /bookings/id  запрос на вещь с id" + id);
        return bookingClient.getById(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookerId(
            @RequestHeader(Constants.headerUserId) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE) @Positive Integer size) {
        BookingState stateEnum = BookingState.stringToState(state).orElseThrow(
                () -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Получен запрос всех вещей бронирующего GET /bookings " + userId);
        return bookingClient.getAllByBookerId(userId, stateEnum, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwnerId(
            @RequestHeader(Constants.headerUserId) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_FROM, required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = Constants.PAGE_DEFAULT_SIZE, required = false) @Positive Integer size) {
        BookingState stateEnum = BookingState.stringToState(state).orElseThrow(
                () -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Получен запрос всех вещей владельца GET /bookings/owner " + userId);
        return bookingClient.getAllByOwnerId(userId, stateEnum, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(Constants.headerUserId) Long userId,
                                  @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Получен запрос POST /bookings " + userId);
        return bookingClient.add(userId, bookingRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader(Constants.headerUserId) Long userId,
                                     @PathVariable Long id,
                                     @RequestParam() Boolean approved) {
        log.info("Получен запрос PATCH /bookings/id " + " ! статус брони вещи с id" + id + ": забронировано=" + approved + " юзер с id" + userId);
        return bookingClient.update(userId, id, approved);
    }
}