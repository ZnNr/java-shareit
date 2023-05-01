package ru.practicum.shareit.booking.enums;

import java.util.Optional;

public enum BookingState {

    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<BookingState> stringToState(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

}
