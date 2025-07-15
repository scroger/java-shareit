package ru.practicum.shareit.item.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Item(
        Long id,
        String name,
        String description,
        Boolean available,
        Long ownerId,
        Long requestId
) {

    public Item withId(Long id) {
        return this.toBuilder()
                .id(id)
                .build();
    }

}
