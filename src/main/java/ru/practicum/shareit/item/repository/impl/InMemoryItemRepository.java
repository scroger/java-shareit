package ru.practicum.shareit.item.repository.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Collection<Item> findByUserId(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.ownerId().equals(userId))
                .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item create(Item item) {
        item = item.withId(nextId());

        items.put(item.id(), item);

        return item;
    }

    @Override
    public Item update(Item item) {
        items.replace(item.id(), item);

        return item;
    }

    @Override
    public Collection<Item> search(String searchText) {
        return items.values()
                .stream()
                .filter(item -> item.available()
                                && (item.name().toUpperCase().contains(searchText.toUpperCase())
                                    || item.description().toUpperCase().contains(searchText.toUpperCase())))
                .toList();
    }

    private Long nextId() {
        return items.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }

}
