package ru.practicum.shareit.user.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record User(
        Long id,
        String name,
        String email
) {

    public User withId(Long id) {
        return this.toBuilder()
                .id(id)
                .build();
    }

}
