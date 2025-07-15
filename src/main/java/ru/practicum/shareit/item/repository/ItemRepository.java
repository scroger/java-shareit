package ru.practicum.shareit.item.repository;

import java.util.Collection;
import java.util.Optional;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {

    Collection<Item> findByUserId(Long userId);

    Optional<Item> findById(Long id);

    Item create(Item item);

    Item update(Item item);

    Collection<Item> search(String searchText);

}
